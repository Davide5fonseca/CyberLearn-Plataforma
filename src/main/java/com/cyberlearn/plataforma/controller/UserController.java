package com.cyberlearn.plataforma.controller;

import com.cyberlearn.plataforma.model.Utilizador;
import com.cyberlearn.plataforma.model.ResultadoQuiz;
import com.cyberlearn.plataforma.model.LogAcesso;
import com.cyberlearn.plataforma.model.Modulo;
import com.cyberlearn.plataforma.repository.UtilizadorRepository;
import com.cyberlearn.plataforma.repository.ResultadoQuizRepository;
import com.cyberlearn.plataforma.repository.LogAcessoRepository;
import com.cyberlearn.plataforma.repository.ModuloRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired private UtilizadorRepository repo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JavaMailSender mailSender;
    @Autowired private ResultadoQuizRepository resultadoRepo;
    @Autowired private LogAcessoRepository logRepo; // Novo Repositório de Logs

    // --- REGISTO DE UTILIZADOR ---
    @PostMapping("/registo")
    public ResponseEntity<?> registo(@RequestBody Utilizador u) {
        if(repo.findByEmail(u.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: E-mail já em uso!");
        }
        u.setPasswordHash(encoder.encode(u.getPasswordHash()));
        u.setRole(u.getPerfil()); 
        repo.save(u);
        return ResponseEntity.ok("Registado com sucesso!");
    }

    // --- LOGIN COM REGISTO DE ACESSO ---
@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilizador login) {
        Optional<Utilizador> userOpt = repo.findByEmail(login.getEmail());
        
        if(userOpt.isPresent() && encoder.matches(login.getPasswordHash(), userOpt.get().getPasswordHash())) {
            Utilizador user = userOpt.get();
            
            // GRAVAR O ACESSO NA BASE DE DADOS
            logRepo.save(new LogAcesso(user)); // Certifique-se que LogAcesso tem este construtor
            
            user.setPasswordHash(null); 
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    // --- ENDPOINT PARA A TABELA DO PROFESSOR ---
    @GetMapping("/ultimos-acessos")
    public ResponseEntity<List<LogAcesso>> obterLogsAcesso() {
        // Retorna todos os logs. O frontend no professor.html trata de inverter a ordem.
        List<LogAcesso> logs = logRepo.findAll();
        logs.forEach(log -> {
            if (log.getUtilizador() != null) {
                log.getUtilizador().setPasswordHash(null); // Segurança
            }
        });
        return ResponseEntity.ok(logs);
    }

    // --- RECUPERAÇÃO DE SENHA ---
    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> pedirRecuperacao(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
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
            return ResponseEntity.ok("E-mail de recuperação enviado!");
        }).orElse(ResponseEntity.badRequest().body("E-mail não encontrado."));
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<?> alterarSenha(@RequestBody ResetSenhaDTO dados) {
        return repo.findByResetToken(dados.token).map(user -> {
            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("O link de recuperação expirou.");
            }
            user.setPasswordHash(encoder.encode(dados.novaSenha));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            repo.save(user);
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        }).orElse(ResponseEntity.badRequest().body("Token inválido ou inexistente."));
    }

    // --- GESTÃO E PERFIL ---
    @GetMapping("/perfil/{email}")
    public ResponseEntity<?> obterPerfil(@PathVariable String email) {
        return repo.findByEmail(email)
                .map(user -> {
                    user.setPasswordHash(null);
                    return ResponseEntity.ok(user);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lista-alunos")
    public ResponseEntity<List<Utilizador>> listarAlunos() {
        List<Utilizador> alunos = repo.findByPerfil("ALUNO");
        alunos.forEach(u -> u.setPasswordHash(null));
        return ResponseEntity.ok(alunos);
    }

    // --- QUIZZES E RELATÓRIOS ---
    @PostMapping("/guardar-nota")
    public ResponseEntity<?> guardarNota(@RequestBody Map<String, Object> dados) {
        String email = (String) dados.get("email");
        return repo.findByEmail(email).map(user -> {
            ResultadoQuiz res = new ResultadoQuiz();
            res.setUtilizador(user);
            res.setCategoria((String) dados.get("categoria"));
            res.setNota((Integer) dados.get("nota"));
            res.setXpGanhos((Integer) dados.get("xp"));
            resultadoRepo.save(res);
            return ResponseEntity.ok("Nota guardada!");
        }).orElse(ResponseEntity.badRequest().body("Utilizador não encontrado"));
    }

    @GetMapping("/relatorio-geral")
    public ResponseEntity<?> obterRelatorioGeral() {
        return ResponseEntity.ok(resultadoRepo.findAllByOrderByDataRealizacaoDesc());
    }

    // No topo do UserController.java
@Autowired private ModuloRepository moduloRepo;

// --- CRIAR NOVO MÓDULO ---
@PostMapping("/criar-modulo")
public ResponseEntity<?> criarModulo(@RequestBody Modulo m) {
    try {
        moduloRepo.save(m);
        return ResponseEntity.ok("Módulo criado com sucesso!");
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Erro ao salvar módulo.");
    }
}

// No UserController.java

// --- LISTAR MÓDULOS PARA OS ALUNOS ---
@GetMapping("/lista-modulos")
public ResponseEntity<List<Modulo>> listarModulos() {
    // Retorna todos os módulos ordenados pelos mais recentes
    return ResponseEntity.ok(moduloRepo.findAll());
}
}