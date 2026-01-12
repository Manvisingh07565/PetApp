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

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.List;

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

        return "/uploads/" + fileName;
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
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        ProfileRequest response = new ProfileRequest();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().toString());

        // Check Role and Fetch Data
        if (user.getRole() == UserRole.OWNER) {
            ownerRepository.findByUser(user).ifPresentOrElse(
                    p -> {
                        response.setFullName(p.getFullName());
                        response.setPhone(p.getPhone());
                        response.setAddress(p.getAddress());
                        response.setPhotoUrl(p.getPhotoUrl());
                    },
                    () -> System.out.println(">>> ⚠️ No Owner Profile Found in DB for this User")
            );
        } else if (user.getRole() == UserRole.VET) {
            vetRepository.findByUser(user).ifPresentOrElse(
                    p -> {
                        response.setFullName(p.getFullName());
                        response.setPhone(p.getPhone());
                        response.setSpecialization(p.getSpecialization());
                        response.setClinicName(p.getClinicName());
                        response.setClinicAddress(p.getClinicAddress());
                        response.setPhotoUrl(p.getPhotoUrl());
                    },
                    () -> System.out.println(">>> ⚠️ No Vet Profile Found in DB for this User")
            );
        }
        return response;
    }

    // --- 4. UPDATE PROFILE ---
    public void updateProfile(ProfileRequest request) {
        System.out.println(">>> SERVICE: Updating Profile for " + request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == UserRole.OWNER) {
            OwnerProfile profile = ownerRepository.findByUser(user).orElse(new OwnerProfile());

            // Set Data
            profile.setUser(user); // Link to User
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setAddress(request.getAddress());
            profile.setPhotoUrl(request.getPhotoUrl());

            // SAVE
            ownerRepository.save(profile);
            System.out.println(">>> ✅ OWNER PROFILE SAVED: " + profile.getFullName());

        } else if (user.getRole() == UserRole.VET) {
            // Find existing profile OR create a new one
            VetProfile profile = vetRepository.findByUser(user).orElse(new VetProfile());

            // Set Data
            profile.setUser(user); // Link to User
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setSpecialization(request.getSpecialization());
            profile.setClinicName(request.getClinicName());
            profile.setClinicAddress(request.getClinicAddress());
            profile.setPhotoUrl(request.getPhotoUrl());

            // SAVE
            vetRepository.save(profile);
            System.out.println(">>> ✅ VET PROFILE SAVED: " + profile.getFullName());
        }
    }
    // --- IMAGE UPLOAD METHOD  ---
    public String saveImage(MultipartFile file) {
        try {
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
    public List<User> findAllVets() {
        return userRepository.findByRole(UserRole.VET); // Make sure your repo has this method
    }
}