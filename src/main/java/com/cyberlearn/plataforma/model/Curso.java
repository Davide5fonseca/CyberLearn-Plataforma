package com.cyberlearn.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter @Setter
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;

    @OneToMany(mappedBy = "curso")
    private List<Modulo> modulos;
}