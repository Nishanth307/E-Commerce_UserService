package dev.auth.userservicetestfinal.secutiry;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.auth.userservicetestfinal.models.Role;
import dev.auth.userservicetestfinal.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@JsonDeserialize(as = CustomUserDetails.class)//deserializing the below class
public class CustomUserDetails implements UserDetails{
      private User user; 
      public CustomUserDetails(){}
      public CustomUserDetails(User user){
            this.user = user;
      }
      @Override 
      @JsonIgnore
      public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
      }

      @Override 
      @JsonIgnore
      public String getPassword() {
            return user.getPassword();
      }

      @Override 
      @JsonIgnore
      public String getUsername() {
            return user.getEmail();
      }

      @Override 
      @JsonIgnore
      public boolean isAccountNonExpired() {
            return true;
      }

      @Override 
      @JsonIgnore
      public boolean isAccountNonLocked() {
            return true;
      }

      @Override 
      @JsonIgnore
      public boolean isCredentialsNonExpired() {
            return true;
      }

      @Override 
      @JsonIgnore
      public boolean isEnabled() {
            return true;
      }
      
}
