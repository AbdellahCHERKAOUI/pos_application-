package com.example.posapplicationapis.repositories;


import com.example.posapplicationapis.entities.Role;
import com.example.posapplicationapis.entities.User;
import com.example.posapplicationapis.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole role);
}

