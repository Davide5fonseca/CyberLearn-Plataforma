package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "modulos")
@Getter @Setter
public class Modulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer ordem;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;
}