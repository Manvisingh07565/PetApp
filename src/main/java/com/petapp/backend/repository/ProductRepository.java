package com.petapp.backend.repository;

import com.petapp.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByCategoryAndPriceBetween(String category, Double minPrice, Double maxPrice);

    List<Product> findByTitleContainingIgnoreCase(String title);
}