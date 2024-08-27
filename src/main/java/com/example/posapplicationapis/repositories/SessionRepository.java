package com.example.posapplicationapis.repositories;


import com.example.posapplicationapis.entities.Session;
import com.example.posapplicationapis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByUser(User user);
}

