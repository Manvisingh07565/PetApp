package com.petapp.backend.dto;

import com.petapp.backend.enums.UserRole;
import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private UserRole role;
    private String requestType;
}