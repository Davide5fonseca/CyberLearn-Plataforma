package com.cyberlearn.plataforma.repository;

import com.cyberlearn.plataforma.model.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAcessoRepository extends JpaRepository<LogAcesso, Long> {
    // Aqui podes adicionar métodos para contar acessos no futuro
}