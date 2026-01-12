package com.petapp.backend.controller;

import com.petapp.backend.dto.AuthRequest;
import com.petapp.backend.dto.VerifyRequest;
import com.petapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody AuthRequest request) {
        try {
            System.out.println(">>> Request received for: " + request.getEmail());
            userService.sendOtp(request);
            return ResponseEntity.ok("OTP sent successfully!");
        } catch (RuntimeException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyRequest request) {
        try {
            String token = userService.verifyOtp(request);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}