package com.petapp.backend.dto;

import com.petapp.backend.enums.ConsultationType;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotRequest {
    private Long vetId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ConsultationType consultationType; // IN_CLINIC, TELECONSULT, BOTH
    private int capacity;
}