package com.cyberlearn.plataforma.controller;

import com.cyberlearn.plataforma.model.Utilizador;
import com.cyberlearn.plataforma.repository.UtilizadorRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*; // RESOLVE O ERRO
import java.util.Map;      // Necessário para ler o JSON

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

@Autowired
private JavaMailSender mailSender;

// Endpoint 1: Gerar token e enviar e-mail
@PostMapping("/recuperar-senha")
public ResponseEntity<?> pedirRecuperacao(@RequestBody Map<String, String> payload) {
    String email = payload.get("email"); // Extrai o email do objeto sem aspas
    
    System.out.println("E-mail recebido: [" + email + "]");

    return repo.findByEmail(email).map(user -> {
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        repo.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Recuperar Senha - CyberLearn");
        message.setText("Link: http://localhost:8080/reset-senha.html?token=" + token);
        
        mailSender.send(message);
        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso!");
    }).orElse(ResponseEntity.badRequest().body("E-mail não encontrado na nossa base de dados."));
}

// Endpoint 2: Validar token e mudar a senha
@PostMapping("/alterar-senha")
public ResponseEntity<?> alterarSenha(@RequestBody ResetSenhaDTO dados) {
    return repo.findByResetToken(dados.token).map(user -> {
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("O link expirou.");
        }
        user.setPasswordHash(encoder.encode(dados.novaSenha));
        user.setResetToken(null); // Limpa o token após o uso
        repo.save(user);
        return ResponseEntity.ok("Senha alterada!");
    }).orElse(ResponseEntity.badRequest().body("Token inválido."));
}
}