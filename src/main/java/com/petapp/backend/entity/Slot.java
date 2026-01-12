package com.petapp.backend.entity;

import com.petapp.backend.enums.ConsultationType;
import jakarta.persistence.*;
import lombok.Data; // Agar Lombok use kar rahe hain, nahi toh Getters/Setters generate karein
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private User vet;

    private LocalDate date; // "2025-12-30"

    private LocalTime startTime; // "10:00"
    private LocalTime endTime;   // "17:00"

    @Enumerated(EnumType.STRING)
    private ConsultationType consultationType; // IN_CLINIC, TELECONSULT, etc.

    private int capacity; // Total pets allowed (e.g., 10)

    private int bookedCount = 0;
}