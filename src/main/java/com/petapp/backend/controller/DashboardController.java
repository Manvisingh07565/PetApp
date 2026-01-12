package com.petapp.backend.controller;

import com.petapp.backend.entity.HealthRecord;
import com.petapp.backend.entity.Pet;
import com.petapp.backend.repository.HealthRecordRepository;
import com.petapp.backend.repository.PetRepository;
import com.petapp.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired private PetRepository petRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private JwtUtils jwtUtils;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestHeader("Authorization") String token) {
        String email = jwtUtils.extractUsername(token.substring(7));

        List<Pet> pets = petRepository.findAll().stream()
                .filter(p -> p.getOwner().getEmail().equals(email)).toList();

        List<HealthRecord> alerts = healthRecordRepository.findUpcomingReminders(email);

        Map<String, Object> data = new HashMap<>();
        data.put("totalPets", pets.size());
        data.put("upcomingCount", alerts.size());
        data.put("reminders", alerts);

        return ResponseEntity.ok(data);
    }
}