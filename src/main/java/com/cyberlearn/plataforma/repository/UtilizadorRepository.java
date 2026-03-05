package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

    // Método para procurar utilizadores pelo e-mail (essencial para Login e Recuperação)
    Optional<Utilizador> findByEmail(String email);

    // Método para procurar o utilizador que tem o token de recuperação enviado por e-mail
    Optional<Utilizador> findByResetToken(String resetToken);
    
}