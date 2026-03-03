package com.cyberlearn.plataforma.controller;

import com.cyberlearn.plataforma.model.Utilizador;
import com.cyberlearn.plataforma.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {
    @Autowired private UtilizadorRepository repo;
    @Autowired private PasswordEncoder encoder;

@PostMapping("/registo")
public ResponseEntity<?> registo(@RequestBody Utilizador u) {
    // Verifica se o email já existe
    if(repo.findByEmail(u.getEmail()).isPresent()) {
        return ResponseEntity.badRequest().body("Erro: Este e-mail já está em uso!");
    }
    
    // Encripta a password antes de guardar
    u.setPasswordHash(encoder.encode(u.getPasswordHash()));
    repo.save(u);
    
    return ResponseEntity.ok("Utilizador registado com sucesso!");
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilizador login) {
        var user = repo.findByEmail(login.getEmail());
        if(user.isPresent() && encoder.matches(login.getPasswordHash(), user.get().getPasswordHash())) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}