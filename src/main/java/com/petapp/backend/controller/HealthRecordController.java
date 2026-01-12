package com.petapp.backend.controller;

import com.petapp.backend.entity.HealthRecord;
import com.petapp.backend.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*") // Frontend (Postman/Browser) ko allow karega
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;

    // 1. ADD RECORD
    // URL: http://localhost:8080/api/health/add?petId=1
    @PostMapping("/add")
    public ResponseEntity<HealthRecord> addRecord(@RequestParam Long petId, @RequestBody HealthRecord record) {
        return ResponseEntity.ok(healthRecordService.addRecord(petId, record));
    }

    // 2. GET HISTORY (TIMELINE)
    // URL: http://localhost:8080/api/health/pet/1
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<HealthRecord>> getRecords(@PathVariable Long petId) {
        return ResponseEntity.ok(healthRecordService.getRecordsByPet(petId));
    }

    // 3. DELETE RECORD
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long id) {
        healthRecordService.deleteRecord(id);
        return ResponseEntity.ok("Record Deleted");
    }
}