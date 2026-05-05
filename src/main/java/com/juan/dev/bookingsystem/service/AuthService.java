package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.dto.AuthRequest;
import com.juan.dev.bookingsystem.model.User;
import com.juan.dev.bookingsystem.model.Role;
import com.juan.dev.bookingsystem.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔥 REGISTER
    public void register(AuthRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        // 🔐 password encriptado
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 🔥 rol por defecto
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    // 🔥 LOGIN (ahora incluye ROLE en el JWT)
    public String login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 🔥 AQUÍ va el cambio importante
        return jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
    }
}