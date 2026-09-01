package com.petapp.backend.controller;

import com.petapp.backend.entity.User;
import com.petapp.backend.repository.UserRepository;
import com.petapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // ✅ 1. GET PROFILE
    @GetMapping("/get-profile")
    public ResponseEntity<?> getProfile(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(404).body("User not found");
    }

    // ✅ 2. UPDATE PROFILE
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedData) {
        Optional<User> userOpt = userRepository.findByEmail(updatedData.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFullName(updatedData.getFullName());
            user.setPhone(updatedData.getPhone());

            // Update Photo URL if provided
            if (updatedData.getPhotoUrl() != null && !updatedData.getPhotoUrl().isEmpty()) {
                user.setPhotoUrl(updatedData.getPhotoUrl());
            }

            // Role specific updates
            if ("VET".equals(user.getRole().toString())) {
                user.setSpecialization(updatedData.getSpecialization());
                user.setClinicName(updatedData.getClinicName());
                user.setClinicAddress(updatedData.getClinicAddress());
            } else {
                user.setAddress(updatedData.getAddress());
            }

            userRepository.save(user);
            return ResponseEntity.ok("Profile Updated");
        }
        return ResponseEntity.status(404).body("User not found");
    }

    // ✅ 3. UPLOAD PHOTO
    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            // Create "uploads" folder if not exists
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the URL path
            return ResponseEntity.ok("/uploads/" + filename);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload Failed");
        }
    }
    @GetMapping("/all-vets")
    public ResponseEntity<List<User>> getAllVets() {
        return ResponseEntity.ok(userService.findAllVets());
    }
}