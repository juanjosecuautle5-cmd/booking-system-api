package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.dto.AuthRequest;
import com.juan.dev.bookingsystem.dto.AuthResponse;
import com.juan.dev.bookingsystem.model.Role;
import com.juan.dev.bookingsystem.model.User;
import com.juan.dev.bookingsystem.model.RefreshToken;
import com.juan.dev.bookingsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // 🔐 LOGIN CON ACCESS + REFRESH (CON PERMISOS 🔥)
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 🔥 EXTRAER PERMISOS DEL ROLE
        List<String> permissions = user.getRole()
                .getPermissions()
                .stream()
                .map(Enum::name)
                .toList();

        // 🔥 ACCESS TOKEN CON PERMISOS
        String accessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                permissions
        );

        // 🔥 REFRESH TOKEN
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    // 🔄 REFRESH TOKEN (ROTACIÓN + PERMISOS)
    public AuthResponse refresh(String refreshToken) {

        // 1. validar token actual
        RefreshToken token = refreshTokenService.validate(refreshToken);

        User user = token.getUser();

        // 2. eliminar token viejo
        refreshTokenService.delete(refreshToken);

        // 3. generar nuevo refresh token
        RefreshToken newRefreshToken = refreshTokenService.create(user);

        // 🔥 EXTRAER PERMISOS DEL ROLE
        List<String> permissions = user.getRole()
                .getPermissions()
                .stream()
                .map(Enum::name)
                .toList();

        // 🔥 NUEVO ACCESS TOKEN CON PERMISOS
        String newAccessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                permissions
        );

        // 5. devolver ambos nuevos
        return new AuthResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );
    }

    // 🚪 LOGOUT
    public void logout(String refreshToken) {
        refreshTokenService.delete(refreshToken);
    }
}