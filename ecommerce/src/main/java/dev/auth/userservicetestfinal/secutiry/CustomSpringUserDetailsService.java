package dev.auth.userservicetestfinal.secutiry;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.auth.userservicetestfinal.models.User;
import dev.auth.userservicetestfinal.repositories.UserRepository;

@Service
public class CustomSpringUserDetailsService implements UserDetailsService{
      private UserRepository userRepository;
      public CustomSpringUserDetailsService(UserRepository userRepository){ 
            this.userRepository = userRepository;
      }
      @Override 
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            //fetch the user with the given from DB
            Optional<User> optionalUser = userRepository.findByEmail(username);//email
            if (optionalUser.isEmpty()){
                  throw new UsernameNotFoundException("username with the given name doesnt exist");
            }
            User user = optionalUser.get();
            return new CustomUserDetails(user); 
            // return new CustomSpringUserDetailsService(user);
      }
      
}
