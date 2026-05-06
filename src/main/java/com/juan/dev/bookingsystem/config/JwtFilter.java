package com.juan.dev.bookingsystem.config;

import com.juan.dev.bookingsystem.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        // 🔹 Si no hay token
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // 🔥 EXTRAER EMAIL
        String email = jwtService.extractEmail(token);

        // 🔹 Evitar sobrescribir autenticación
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // 🔥 VALIDAR TOKEN
            if (jwtService.isTokenValid(token, email)) {

                // 🔥 EXTRAER PERMISOS
                List<String> permissions =
                        jwtService.extractPermissions(token);

                // 🔥 CONVERTIR A AUTHORITIES
                var authorities = permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // 🔥 CREAR AUTH
                var auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorities
                );

                // 🔥 SETEAR EN CONTEXTO
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}