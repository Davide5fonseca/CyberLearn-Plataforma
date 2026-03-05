package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_acesso")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAcesso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilizador_id")
    private Utilizador utilizador;

    private LocalDateTime dataHoraAcesso = LocalDateTime.now();
    
    // Construtor auxiliar
    public LogAcesso(Utilizador utilizador) {
        this.utilizador = utilizador;
        this.dataHoraAcesso = LocalDateTime.now();
    }
}