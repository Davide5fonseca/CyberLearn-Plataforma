package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilizadores")
@Getter @Setter
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private String nome; 
    private String email;
    private String passwordHash; 
    private String perfil; // Aluno, Professor ou Administrador [cite: 441]
    private LocalDateTime dataRegisto = LocalDateTime.now(); 
    private boolean ativo = true; 
}