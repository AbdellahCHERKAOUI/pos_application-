package com.example.posapplicationapis.repositories;


import com.example.posapplicationapis.entities.User;
import com.example.posapplicationapis.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserByRolesName(ERole roleName);
}
