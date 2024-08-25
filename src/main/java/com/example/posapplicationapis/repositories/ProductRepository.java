package com.example.posapplicationapis.repositories;

import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> getAllByCategory(Category category);
}
