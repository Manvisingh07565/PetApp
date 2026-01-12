package com.petapp.backend.controller;

import com.petapp.backend.entity.Product;
import com.petapp.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Sabke liye: Products dekhne ke liye
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Sirf Admin ke liye: Product add karne ke liye
    @PostMapping("/admin/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }
    @GetMapping("/filter")
    public List<Product> getFilteredProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") Double minPrice,
            @RequestParam(defaultValue = "10000") Double maxPrice) {

        if (category != null && !category.isEmpty() && !category.equals("All")) {
            return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.ok("Product Deleted Successfully");
        }).orElse(ResponseEntity.notFound().build());
    }
}