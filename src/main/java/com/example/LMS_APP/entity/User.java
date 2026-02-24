package com.example.LMS_APP.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password; // hashed

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, length = 100)
    private String fullName;

    // 🔥 NEW FIELD ADDED
    @Column(nullable = false)
    private int leaveBalance = 15;   // Default 15 days

    public User() {}

    public User(String username, String password, UserRole role, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.leaveBalance = 15;  // default when new user created
    }

    // ===== GETTERS =====

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public String getFullName() { return fullName; }
    public int getLeaveBalance() { return leaveBalance; }

    // ===== SETTERS =====

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(UserRole role) { this.role = role; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setLeaveBalance(int leaveBalance) { this.leaveBalance = leaveBalance; }
}