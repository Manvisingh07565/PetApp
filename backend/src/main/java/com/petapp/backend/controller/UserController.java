package com.petapp.backend.controller;

import com.petapp.backend.dto.ProfileRequest;
import com.petapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            String photoUrl = userService.uploadImage(file);
            return ResponseEntity.ok(photoUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed");
        }
    }

    @PostMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileRequest request) {
        userService.updateProfile(request);
        return ResponseEntity.ok("Profile updated successfully!");
    }

    @GetMapping("/get-profile")
    public ResponseEntity<ProfileRequest> getProfile(@RequestParam String email) {
        ProfileRequest profile = userService.getProfile(email);
        if (profile != null) {
            return ResponseEntity.ok(profile); // 200 OK with Data
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}