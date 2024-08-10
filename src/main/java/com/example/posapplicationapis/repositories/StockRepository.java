package com.example.posapplicationapis.repositories;


import com.example.posapplicationapis.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s JOIN s.ingredientStockMap ism WHERE KEY(ism).id = :ingredientId")
    Optional<Stock> findByIngredientId(@Param("ingredientId") Long ingredientId);
}


