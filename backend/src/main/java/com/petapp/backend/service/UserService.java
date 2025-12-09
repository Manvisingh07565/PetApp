package com.petapp.backend.service;

import com.petapp.backend.dto.AuthRequest;
import com.petapp.backend.dto.ProfileRequest;
import com.petapp.backend.dto.VerifyRequest;
import com.petapp.backend.entity.OwnerProfile;
import com.petapp.backend.entity.User;
import com.petapp.backend.entity.VetProfile;
import com.petapp.backend.enums.UserRole;
import com.petapp.backend.repository.OwnerRepository;
import com.petapp.backend.repository.UserRepository;
import com.petapp.backend.repository.VetRepository;
import com.petapp.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtils jwtUtils;

    public void sendOtp(AuthRequest request) {
        System.out.println(">>> Sending OTP to: " + request.getEmail());
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        User user;
        boolean isNewUser = false;

        if ("REGISTER".equalsIgnoreCase(request.getRequestType())) {
            if (existingUser.isPresent()) {
                throw new RuntimeException("User already exists! Please Login.");
            }
            user = new User();
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());
            isNewUser = true;
        } else {
            if (existingUser.isEmpty()) {
                throw new RuntimeException("User not found! Please Register.");
            }
            user = existingUser.get();
            isNewUser = false;
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp, isNewUser);
    }
    private final String UPLOAD_DIR = "uploads/";

    public String uploadImage(MultipartFile file) throws Exception {
        if (file.isEmpty()) return null;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName; // Browser ke liye URL
    }

    public String verifyOtp(VerifyRequest request) {
        System.out.println(">>> Verifying OTP for: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setVerified(true);
        user.setOtp(null);
        userRepository.save(user);

        return jwtUtils.generateToken(user.getEmail());
    }

    public ProfileRequest getProfile(String email) {
        if (email == null || email.isEmpty()) return null;

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        ProfileRequest response = new ProfileRequest();

        response.setEmail(user.getEmail());
        response.setRole(user.getRole().toString());

        if (user.getRole() == UserRole.OWNER) {
            Optional<OwnerProfile> owner = ownerRepository.findByUser(user);
            if (owner.isPresent()) {
                OwnerProfile p = owner.get();
                response.setFullName(p.getFullName());
                response.setPhone(p.getPhone());
                response.setAddress(p.getAddress());
                response.setPhotoUrl(p.getPhotoUrl());
            }
        } else if (user.getRole() == UserRole.VET) {
            Optional<VetProfile> vet = vetRepository.findByUser(user);
            if (vet.isPresent()) {
                VetProfile p = vet.get();
                response.setFullName(p.getFullName());
                response.setPhone(p.getPhone());
                response.setPhotoUrl(p.getPhotoUrl());
                response.setSpecialization(p.getSpecialization());
                response.setClinicName(p.getClinicName());
                response.setClinicAddress(p.getClinicAddress());
            }
        }

        return response;
    }

    // --- 4. UPDATE PROFILE ---
    public void updateProfile(ProfileRequest request) {
        System.out.println(">>> Updating Profile for: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == UserRole.OWNER) {
            OwnerProfile profile = ownerRepository.findByUser(user).orElse(new OwnerProfile());
            profile.setUser(user);
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setAddress(request.getAddress());
            profile.setPhotoUrl(request.getPhotoUrl());
            ownerRepository.save(profile);
        } else if (user.getRole() == UserRole.VET) {
            VetProfile profile = vetRepository.findByUser(user).orElse(new VetProfile());
            profile.setUser(user);
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setSpecialization(request.getSpecialization());
            profile.setClinicName(request.getClinicName());
            profile.setClinicAddress(request.getClinicAddress());
            profile.setPhotoUrl(request.getPhotoUrl());
            vetRepository.save(profile);
        }
    }
}