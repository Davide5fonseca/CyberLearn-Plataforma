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
            // 1. Permitir recursos estáticos e APIs de acesso
            .requestMatchers(
                "/index.html", "/registo.html", "/recuperar.html", "/reset-senha.html",
                "/css/**", "/js/**", "/img/**",
                "/api/usuarios/**" // Permite todas as chamadas à API de utilizadores
            ).permitAll()

            // 2. Permitir que o navegador carregue as páginas (a proteção será via JS abaixo)
            .requestMatchers(
                "/dashboard.html", "/quiz.html", "/modulos.html", 
                "/perfil.html", "/professor.html","/alunos.html"
            ).permitAll() 

            .anyRequest().authenticated()
        )
        .formLogin(form -> form.disable());
    return http.build();
}
}