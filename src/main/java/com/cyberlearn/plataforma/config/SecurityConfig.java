package com.cyberlearn.plataforma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Permite ver o login, o registo e o dashboard sem bloqueios iniciais
            .requestMatchers("/index.html", "/registo.html", "/dashboard.html", "/css/**", "/api/usuarios/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form.disable()); // Desativa o formulário padrão cinzento
    return http.build();
}
    }
