package com.juan.dev.bookingsystem.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "my-super-secret-key-my-super-secret-key";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔥 NUEVO: token con ROLE
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // 👈 AQUÍ guardamos el rol
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔥 OPCIONAL (puedes eliminar el viejo si quieres)
    public String generateToken(String email) {
        return generateToken(email, "USER");
    }

    // 🔥 EXTRAER EMAIL
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 🔥 NUEVO: EXTRAER ROLE
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // 🔥 adaptador para el filtro
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
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}