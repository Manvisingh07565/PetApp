package com.petapp.backend.service;

import com.petapp.backend.entity.HealthRecord;
import com.petapp.backend.entity.Pet;
import com.petapp.backend.repository.HealthRecordRepository;
import com.petapp.backend.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private PetRepository petRepository;

    // 1. Add Record
    public HealthRecord addRecord(Long petId, HealthRecord record) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        record.setPet(pet);

        return healthRecordRepository.save(record);
    }

    // 2. Get Records
    public List<HealthRecord> getRecordsByPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        return healthRecordRepository.findByPet(pet);
    }

    // 3. Delete Record
    public void deleteRecord(Long recordId) {
        healthRecordRepository.deleteById(recordId);
    }
}