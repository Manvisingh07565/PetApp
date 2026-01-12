package com.petapp.backend.dto;

import com.petapp.backend.enums.ConsultationType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentRequest {
    private Long ownerId;
    private Long vetId;
    private Long slotId;
    private LocalDate appointmentDate;
    private String petName;
    private String reason;
    private ConsultationType type;
    private String diagnosis;
    private String medicines;

}