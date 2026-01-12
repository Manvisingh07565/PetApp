package com.petapp.backend.service;

import com.petapp.backend.entity.Pet;
import com.petapp.backend.entity.User;
import com.petapp.backend.repository.PetRepository;
import com.petapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    // --- 1. ADD PET ---
    public Pet addPet(String userEmail, Pet pet) {
        // Owner dhundo
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        pet.setOwner(owner);

        if (pet.getPhotoUrl() == null || pet.getPhotoUrl().isEmpty()) {
            pet.setPhotoUrl("default-pet.png");
        }

        return petRepository.save(pet);
    }

    // --- 2. GET MY PETS ---
    public List<Pet> getPetsByOwner(String userEmail) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        return petRepository.findByOwner(owner);
    }

    // --- 3. DELETE PET ---
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }
}