package dev.auth.userservicetestfinal.secutiry;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.auth.userservicetestfinal.models.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonDeserialize(as = CustomGrantedAuthority.class)
public class CustomGrantedAuthority implements GrantedAuthority{
      private Role role;

      public  CustomGrantedAuthority(Role role){
            this.role = role;
      }
      @Override
      @JsonIgnore
      public String getAuthority() {
            return role.getRole(); 
      }
      
}
 