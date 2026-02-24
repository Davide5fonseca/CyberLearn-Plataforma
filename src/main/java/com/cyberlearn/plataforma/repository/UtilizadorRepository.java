package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    Optional<Utilizador> findByEmail(String email);
}