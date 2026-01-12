package com.petapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "health_records")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Common Fields
    private LocalDate recordDate; // Visit Date or Vaccination Date
    private String type;          // "VACCINATION" or "MEDICAL_VISIT"
    private String vetName;       // Doctor Name
    private String notes;         // General Notes

    // Medical Specific
    private String diagnosis;     // What happened?
    private String treatment;     // What treatment was given?
    private String prescription;  // Medicines prescribed
    private String attachmentUrl; // Report/File URL

    // Vaccination Specific
    private String vaccineName;   // Name of Vaccine (e.g. Rabies)
    private LocalDate nextDueDate;// When is the next dose?
    // ... existing fields ...

    private String repeatFrequency; // Values: "NONE", "MONTHLY", "YEARLY"

    // Link to Pet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonIgnore
    private Pet pet;
}