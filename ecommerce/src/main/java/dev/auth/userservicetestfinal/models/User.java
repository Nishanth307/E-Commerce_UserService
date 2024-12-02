package dev.auth.userservicetestfinal.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Getter
@Setter
@JsonDeserialize(as = User.class)
public class User extends BaseModel {
    private String email;
    private String password;
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();
}
