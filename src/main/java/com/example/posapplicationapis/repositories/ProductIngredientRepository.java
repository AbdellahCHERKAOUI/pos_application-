package com.example.posapplicationapis.repositories;



import com.example.posapplicationapis.entities.ProductIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductIngredientRepository extends JpaRepository<ProductIngredient, Long> {
}
