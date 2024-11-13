package dev.auth.userservicetestfinal.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import dev.auth.userservicetestfinal.dtos.UserDto;
import dev.auth.userservicetestfinal.exceptions.IncorrectPasswordException;
import dev.auth.userservicetestfinal.exceptions.SessionLimitExceededException;
import dev.auth.userservicetestfinal.exceptions.SessionNotFoundException;
import dev.auth.userservicetestfinal.exceptions.UserNotFoundException;
import dev.auth.userservicetestfinal.models.Role;
import dev.auth.userservicetestfinal.models.Session;
import dev.auth.userservicetestfinal.models.SessionStatus;
import dev.auth.userservicetestfinal.models.User;
import dev.auth.userservicetestfinal.repositories.SessionRepository;
import dev.auth.userservicetestfinal.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDto> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
//implement deleting token if there are more than two active sessions of a user
        if (userOptional.isEmpty()) {
            // return null;throw user not found exception
            throw new UserNotFoundException();
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException();
            //return null; throw incorrect password exception
        }

        //Generating the token
        //String token = RandomStringUtils.randomAlphanumeric(30);
        MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
        SecretKey key = alg.key().build();
        String message = "Hello world";
        //        String message = "{\n" +
        //                "  \"email\": \"harsh@scaler.com\",\n" +
        //                "  \"roles\": [\n" +
        //                "    \"student\",\n" +
        //                "    \"ta\"\n" +
        //                "  ],\n" +
        //                "  \"expiry\": \"31stJan2024\"\n" +
        //                "}";
        //JSON -> Key : Value
        
        HashMap<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("email", user.getEmail());
        jsonMap.put("roles", List.of(user.getRoles()));
        jsonMap.put("createdAt", new Date());
        jsonMap.put("expiringAt", DateUtils.addDays(new Date(), 30));
        /*
         
        byte[] content = message.getBytes(StandardCharsets.UTF_8);
        String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();
        String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();
        Parse the compact JWS:
        content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();
        assert message.equals(new String(content, StandardCharsets.UTF_8));
        */
        String jws = Jwts.builder().claims(jsonMap).signWith(key, alg).compact();

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(jws);//jws is token
        session.setUser(user);
        //session.setExpiringAt(current time + 30 days);
        session.setExpiringAt(DateUtils.addDays(new Date(), 30));
        sessionRepository.save(session);

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + jws);//setting cookie

        limitActiveSessions(user);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);//in headers we are passing cookie
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            // return null;
            throw new SessionNotFoundException();
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // We should store the encrypted password in the DB for a user.
        
        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            throw new SessionNotFoundException();
            // return null;
        }
        Session session = new Session();
        if (!session.getSessionStatus().equals(SessionStatus.ENDED)){
            return SessionStatus.ENDED;
        }
        Date currenDate  = new Date(); 
        if (session.getExpiringAt().before(currenDate)){
            return null;//Session is expired
            }  
        
        // JWT Decoding, payload is json part
        //Map<String,Object>  -> payload object or JSON 
        Jws<Claims> jwsClaims = Jwts.parser().build().parseSignedClaims(token);
        String email = (String)   jwsClaims.getPayload().get("email");  
        List<Role> roles = (List<Role>) jwsClaims.getPayload().get("roles");
        Date createdAt = (Date) jwsClaims.getPayload().get("createdAt");  

        // if (restrictedEmail.contains(email)){
        //     //reject token
        // }

        return SessionStatus.ACTIVE;
    }

    public void limitActiveSessions(User user){
        List<Session> sessions = sessionRepository.findAllByUserIdAndSessionStatus(user.getId(),1);
        Date currenDate = new Date();
        for (Session session:sessions){
            System.out.println(session.getExpiringAt()+" "+session.getId());
        }
        if (sessions.size()> 2){
            throw new SessionLimitExceededException();
        }
          
    }

}
