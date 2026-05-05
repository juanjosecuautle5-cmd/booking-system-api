package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.dto.AuthRequest;
import com.juan.dev.bookingsystem.dto.AuthResponse;
import com.juan.dev.bookingsystem.model.Role;
import com.juan.dev.bookingsystem.model.User;
import com.juan.dev.bookingsystem.model.RefreshToken;
import com.juan.dev.bookingsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    // 🔐 REGISTER
    public void register(AuthRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());

        // password encriptado
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // rol por defecto
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    // 🔐 LOGIN CON ACCESS + REFRESH
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Access Token (JWT)
        String accessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // Refresh Token (DB)
        var refreshToken = refreshTokenService.create(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    // 🔄 REFRESH TOKEN
    public AuthResponse refresh(String refreshToken) {

        // validar refresh token
        RefreshToken token = refreshTokenService.validate(refreshToken);

        User user = token.getUser();

        // generar nuevo access token
        String newAccessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(newAccessToken, refreshToken);
    }
}