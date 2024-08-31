package com.example.posapplicationapis.repositories;



import com.example.posapplicationapis.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByDate(LocalDateTime date);

    @Query("SELECT o FROM Order o WHERE DATE(o.date) = :date")
    List<Order> findOrdersByDay(@Param("date") LocalDate date);
}
