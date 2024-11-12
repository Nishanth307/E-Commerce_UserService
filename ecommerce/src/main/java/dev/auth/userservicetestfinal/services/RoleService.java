package dev.auth.userservicetestfinal.services;

import org.springframework.stereotype.Service;

import dev.auth.userservicetestfinal.models.Role;
import dev.auth.userservicetestfinal.repositories.RoleRepository;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name) {
        Role role = new Role();
        role.setRole(name);

        return roleRepository.save(role);
    }
}
