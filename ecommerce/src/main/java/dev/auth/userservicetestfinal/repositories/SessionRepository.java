package dev.auth.userservicetestfinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.auth.userservicetestfinal.models.Session;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
    List<Session> findAllByUserIdAndSessionStatus(Long id,Integer status);
}
