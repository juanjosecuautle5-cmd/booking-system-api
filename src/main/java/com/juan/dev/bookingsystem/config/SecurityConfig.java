package com.juan.dev.bookingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // 🔥 IMPORTANTE
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // 🔥 ACTIVA @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 🔒 Desactiva CSRF (para APIs REST con JWT)
            .csrf(csrf -> csrf.disable())

            // 🔐 Reglas de seguridad
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // login, register, refresh, logout
                .anyRequest().authenticated() // todo lo demás requiere token
            )

            // 🔥 Filtro JWT antes del filtro de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 Password encoder (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}