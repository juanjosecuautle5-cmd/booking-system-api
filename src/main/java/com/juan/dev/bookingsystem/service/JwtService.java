package com.juan.dev.bookingsystem.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final String SECRET = "my-super-secret-key-my-super-secret-key";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔥 TOKEN CON ROLE + PERMISSIONS
    public String generateToken(String email, String role, List<String> permissions) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔥 OPCIONAL (compatibilidad)
    public String generateToken(String email, String role) {
        return generateToken(email, role, List.of());
    }

    // 🔥 EXTRAER EMAIL
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 🔥 EXTRAER ROLE
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 🔥 EXTRAER PERMISSIONS
    public List<String> extractPermissions(String token) {
        return getClaims(token).get("permissions", List.class);
    }

    // 🔥 helper central
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔥 adaptador Spring Security
    public String extractUsername(String token) {
        return extractEmail(token);
    }

    // 🔥 validar token con email
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && isValid(token);
    }

    // 🔥 validar token general
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}