package com.petapp.backend.controller;

import com.petapp.backend.entity.CartItem;
import com.petapp.backend.entity.Product;
import com.petapp.backend.entity.User;
import com.petapp.backend.repository.CartRepository;
import com.petapp.backend.repository.ProductRepository;
import com.petapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    @PostMapping("/add/{ownerId}/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Long ownerId, @PathVariable Long productId) {
        User owner = userRepository.findById(ownerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Optional<CartItem> existingItem = cartRepository.findByOwnerAndProduct(owner, product);

        if(existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setOwner(owner);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartRepository.save(newItem);
        }
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping("/{ownerId}")
    public List<CartItem> getCart(@PathVariable Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow();
        return cartRepository.findByOwner(owner);
    }
}