package com.petapp.backend.entity;

import com.petapp.backend.enums.AppointmentStatus;
import com.petapp.backend.enums.ConsultationType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Appointment {
    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;
    @Column(name = "medicines", length = 1000)
    private String medicines;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private User vet;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    private LocalDate appointmentDate;

    private String petName;
    private String reason; // e.g., "Fever", "Vaccination"

    @Enumerated(EnumType.STRING)
    private ConsultationType type;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING; // Default Pending

    private String meetingLink;
    // Diagnosis ka getter
    public String getDiagnosis() {
        return diagnosis;
    }

    // Medicines ka getter
    public String getMedicines() {
        return medicines;
    }

    // Inhe bhi add kar dein taaki set karne mein problem na ho
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }
}