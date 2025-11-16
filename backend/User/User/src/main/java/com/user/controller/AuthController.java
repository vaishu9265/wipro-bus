package com.user.controller;

import com.user.entity.LoginRequest;
import com.user.entity.VerifyLoginOtpRequest;
import com.user.entity.VerifyOtpRequest;
import com.user.entity.RegisterRequest;
import com.user.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    // ⬅ REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }


    // ⬅ VERIFY REGISTER OTP
    @PostMapping("/verify-register-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest request) {
        return authService.verifyRegisterOtp(request.getEmail(), request.getOtp());
    }



    // ⬅ LOGIN → SEND OTP
    @PostMapping("/login")
    public ResponseEntity<?> loginSendOtp(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginSendOtp(request));
    }


    // ⬅ VERIFY LOGIN OTP → GENERATE JWT
    @PostMapping("/verify-login-otp")
    public ResponseEntity<?> verifyLoginOtp(@RequestBody VerifyLoginOtpRequest request) {
        return ResponseEntity.ok(authService.verifyLoginOtp(request.getEmail(), request.getOtp()));
    }


    // ⬅ FORGOT PASSWORD → SEND OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(authService.forgotPassword(email));
    }


    // ⬅ RESET PASSWORD USING OTP
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {
        return ResponseEntity.ok(authService.resetPassword(email, otp, newPassword));
    }
}
