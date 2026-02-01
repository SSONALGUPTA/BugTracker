
package com.bugtracker.controller;

import com.bugtracker.entity.User;
import com.bugtracker.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final UserService service;
    private final com.bugtracker.repository.UserRepository userRepo;

    public AuthController(UserService service, com.bugtracker.repository.UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public com.bugtracker.dto.AuthResponse register(@RequestBody User u) {
        System.out.println("Register request received for: " + u.getEmail());
        try {
            String token = service.register(u);
            System.out.println("Registration successful for: " + u.getEmail());
            return new com.bugtracker.dto.AuthResponse(token, u);
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/login")
    public com.bugtracker.dto.AuthResponse login(@RequestBody User u) {
        String token = service.login(u.getEmail(), u.getPassword());
        if (token != null) {
            User user = userRepo.findByEmail(u.getEmail()).orElse(null);
            return new com.bugtracker.dto.AuthResponse(token, user);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
