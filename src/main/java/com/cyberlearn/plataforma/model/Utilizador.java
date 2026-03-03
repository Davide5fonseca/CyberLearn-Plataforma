package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.*; // Importa tudo do lombok
import java.time.LocalDateTime;

@Entity
@Table(name = "utilizadores")
@Getter @Setter
@NoArgsConstructor  // Adicione esta: Construtor sem argumentos (necessário para Jackson/JPA)
@AllArgsConstructor // Adicione esta: Construtor com todos os argumentos
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    private String nome; 
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String passwordHash; 
    private String perfil; 
    
    private LocalDateTime dataRegisto = LocalDateTime.now(); 
    private boolean ativo = true; 
}