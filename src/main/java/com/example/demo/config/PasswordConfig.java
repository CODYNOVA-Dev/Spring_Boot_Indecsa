package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configura el algoritmo de hashing de contraseñas (BCrypt).
 *
 * Importante: usamos solo spring-security-crypto, NO el starter completo de
 * Spring Security — así no se activan filtros de autenticación globales que
 * romperían los demás endpoints abiertos.
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Strength 10 (default). 12 sería más seguro pero ~4x más lento por login.
        return new BCryptPasswordEncoder();
    }
}
