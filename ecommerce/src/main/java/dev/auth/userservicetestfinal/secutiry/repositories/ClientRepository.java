package dev.auth.userservicetestfinal.secutiry.repositories;

import dev.auth.userservicetestfinal.secutiry.models.Client;

import java.util.Optional;

//import sample.jpa.entity.client.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);
}
