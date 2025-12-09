package com.petapp.backend.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String email;
    private String role;
    private String fullName;
    private String phone;
    private String address;
    private String photoUrl;

    // Vet fields
    private String specialization;
    private String clinicName;
    private String clinicAddress;
}