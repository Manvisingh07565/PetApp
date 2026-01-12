package com.petapp.backend.dto;

import com.petapp.backend.enums.UserRole;

public class AuthRequest {
    private String email;
    private UserRole role;
    private String requestType;
    private String otp; // I added this back because your Verify logic likely needs it!

    // --- MANUAL GETTERS AND SETTERS ---

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}