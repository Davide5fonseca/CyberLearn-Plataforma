package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.ResultadoQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultadoQuizRepository extends JpaRepository<ResultadoQuiz, Long> {
    List<ResultadoQuiz> findByUtilizadorEmail(String email);
    List<ResultadoQuiz> findAllByOrderByDataRealizacaoDesc();
}