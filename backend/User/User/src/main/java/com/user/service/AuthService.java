package com.user.service;

import com.user.config.JwtUtil;
import com.user.entity.LoginRequest;
import com.user.entity.RegisterRequest;
import com.user.entity.User;
import com.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    // -----------------------------------------------------------
    // REGISTER USER (OTP SENT)
    // -----------------------------------------------------------
    public Map<String, Object> registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setVerified(false);

        // Generate OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully. Verify OTP.");
        response.put("email", request.getEmail());
        response.put("otp", otp); // returning otp in JSON
        return response;
    }


    // -----------------------------------------------------------
    // VERIFY REGISTER OTP
    // -----------------------------------------------------------
    public String verifyRegisterOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otp.equals(user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Registration verified successfully";
    }


    // -----------------------------------------------------------
    // LOGIN → SEND OTP (NO JWT)
    // -----------------------------------------------------------
    public Map<String, Object> loginSendOtp(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("User not verified. Please verify registration OTP first.");
        }

        // authenticate user using Spring Security
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate OTP for login
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login OTP sent");
        response.put("email", request.getEmail());
        response.put("otp", otp); // returning OTP directly
        return response;
    }


    // -----------------------------------------------------------
    // VERIFY LOGIN OTP → GENERATE JWT
    // -----------------------------------------------------------
    public Map<String, Object> verifyLoginOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otp.equals(user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // Clear OTP
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        // Generate JWT
        String token = jwtUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("userId", user.getId());

        return response;
    }


    // -----------------------------------------------------------
    // FORGOT PASSWORD (SEND OTP)
    // -----------------------------------------------------------
    public Map<String, Object> forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "OTP sent for password reset");
        response.put("email", email);
        response.put("otp", otp);

        return response;
    }


    // -----------------------------------------------------------
    // RESET PASSWORD
    // -----------------------------------------------------------
    public String resetPassword(String email, String otp, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otp.equals(user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return "Password reset successfully";
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
