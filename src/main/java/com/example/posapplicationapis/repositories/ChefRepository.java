package com.example.posapplicationapis.repositories;


import com.example.posapplicationapis.entities.Chef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
}

