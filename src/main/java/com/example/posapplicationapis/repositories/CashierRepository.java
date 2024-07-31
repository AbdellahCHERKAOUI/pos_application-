package com.example.posapplicationapis.repositories;
import com.example.posapplicationapis.entities.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, Long> {
}
