package com.petapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMP: Added this import
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "pet_vitals")
public class PetVital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate recordDate;
    private Double weight;      // in Kg
    private Double temperature; // in Celsius
    private String notes;       // Notes field

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonIgnore
    private Pet pet;
}