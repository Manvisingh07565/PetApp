package com.petapp.backend.repository;

import com.petapp.backend.entity.CartItem;
import com.petapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByOwner(User owner);
    Optional<CartItem> findByOwnerAndProduct(User owner, com.petapp.backend.entity.Product product);
}