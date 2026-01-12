package com.petapp.backend.controller;

import com.petapp.backend.entity.Pet;
import com.petapp.backend.service.PetService;
import com.petapp.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private JwtUtils jwtUtils;

    private String getEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtils.extractUsername(authHeader.substring(7));
        }
        throw new RuntimeException("Invalid Token");
    }

    // --- 1. ADD PET ---
    @PostMapping("/add")
    public ResponseEntity<?> addPet(@RequestHeader("Authorization") String token, @RequestBody Pet pet) {
        try {
            String email = getEmailFromToken(token);
            System.out.println(">>> ADD PET REQUEST: " + pet.getName()); // Debug print

            Pet newPet = petService.addPet(email, pet);
            return ResponseEntity.ok(newPet);
        } catch (Exception e) {
            e.printStackTrace(); // Error console me dikhega
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // --- 2. GET MY PETS ---
    @GetMapping("/my-pets")
    public ResponseEntity<List<Pet>> getMyPets(@RequestHeader("Authorization") String token) {
        String email = getEmailFromToken(token);
        return ResponseEntity.ok(petService.getPetsByOwner(email));
    }

    // --- 3. DELETE PET ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok("Pet Deleted");
    }

    // --- Add this API Endpoint ---
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }
}