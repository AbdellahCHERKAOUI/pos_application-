package com.example.posapplicationapis.repositories;


import com.example.posapplicationapis.entities.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplementRepository extends JpaRepository<Supplement, Long> {
}


