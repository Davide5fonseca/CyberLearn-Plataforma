package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}