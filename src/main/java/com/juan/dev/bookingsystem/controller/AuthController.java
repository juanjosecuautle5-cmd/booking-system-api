package com.juan.dev.bookingsystem.controller;

import com.juan.dev.bookingsystem.dto.AuthRequest;
import com.juan.dev.bookingsystem.dto.AuthResponse;
import com.juan.dev.bookingsystem.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🔥 REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered");
    }

    // 🔥 LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}