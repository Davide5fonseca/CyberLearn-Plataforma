// No ficheiro UtilizadorRepository.java
package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List; // Adicione esta importação

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    Optional<Utilizador> findByEmail(String email);
    Optional<Utilizador> findByResetToken(String resetToken);
    List<Utilizador> findByPerfil(String perfil);
}