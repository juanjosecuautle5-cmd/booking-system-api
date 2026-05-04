package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.dto.AuthRequest;
import com.juan.dev.bookingsystem.model.User;
import com.juan.dev.bookingsystem.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    // 🔥 Registro
    public void register(AuthRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);
    }

    // 🔥 Login → ahora devuelve JWT
    public String login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        return jwtService.generateToken(user.getEmail());
    }
}