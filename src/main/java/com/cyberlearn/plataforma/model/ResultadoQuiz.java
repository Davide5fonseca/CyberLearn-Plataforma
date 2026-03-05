package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_quizzes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilizador_id")
    private Utilizador utilizador;

    private String categoria;
    private Integer nota;
    private Integer xpGanhos;
    private LocalDateTime dataRealizacao = LocalDateTime.now();
}