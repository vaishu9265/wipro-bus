package com.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "USER";

    // 👉 NEW FIELD – profile verification status
    @Column(nullable = false)
    private boolean verified = false;
    
    private String otp;
    private LocalDateTime otpExpiry;

    public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getOtpExpiry() {
		return otpExpiry;
	}

	public void setOtpExpiry(LocalDateTime otpExpiry) {
		this.otpExpiry = otpExpiry;
	}
	// 👉 NEW FIELD – profile create timestamp
    @Column(nullable = false)
    private LocalDateTime profileCreatedAt = LocalDateTime.now();

    public User() {}

    

    public User(Long id, String name, String email, String phone, String password, String role, boolean verified,
			String otp, LocalDateTime otpExpiry, LocalDateTime profileCreatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.verified = verified;
		this.otp = otp;
		this.otpExpiry = otpExpiry;
		this.profileCreatedAt = profileCreatedAt;
	}

	// Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public LocalDateTime getProfileCreatedAt() { return profileCreatedAt; }
    public void setProfileCreatedAt(LocalDateTime profileCreatedAt) { this.profileCreatedAt = profileCreatedAt; }
}
