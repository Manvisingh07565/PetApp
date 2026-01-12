package com.petapp.backend.controller;

import com.petapp.backend.entity.Pet;
import com.petapp.backend.entity.PetVital;
import com.petapp.backend.repository.PetRepository;
import com.petapp.backend.repository.PetVitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vitals")
@CrossOrigin(origins = "*")
public class PetVitalController {

    @Autowired private PetVitalRepository vitalRepo;
    @Autowired private PetRepository petRepo;

    // 1. Add Vital Record
    @PostMapping("/add")
    public ResponseEntity<?> addVital(@RequestParam Long petId, @RequestBody PetVital vital) {
        Pet pet = petRepo.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        vital.setPet(pet);

        // If date is missing, use today
        if (vital.getRecordDate() == null) {
            vital.setRecordDate(LocalDate.now());
        }

        vitalRepo.save(vital);
        return ResponseEntity.ok("Vital Added");
    }

    // 2. Get Vitals for Chart
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<PetVital>> getVitals(@PathVariable Long petId) {
        return ResponseEntity.ok(vitalRepo.findByPetIdOrderByRecordDateAsc(petId));
    }

    // 3. Delete Vital Record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVital(@PathVariable Long id) {
        vitalRepo.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}