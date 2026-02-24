package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "perguntas")
@Getter @Setter
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String enunciado;

    @Column(nullable = false)
    private String opcaoA;

    @Column(nullable = false)
    private String opcaoB;

    @Column(nullable = false)
    private String opcaoC;

    @Column(nullable = false)
    private String opcaoD;

    @Column(nullable = false, length = 1)
    private String respostaCorreta; // Armazena 'A', 'B', 'C' ou 'D'

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonIgnore // Evita erros de recursão infinita ao enviar JSON para o frontend
    private Quiz quiz;
}