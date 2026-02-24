package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}