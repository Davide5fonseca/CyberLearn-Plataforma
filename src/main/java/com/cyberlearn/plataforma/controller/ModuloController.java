package com.cyberlearn.plataforma.controller;

import com.cyberlearn.plataforma.model.Modulo;
import com.cyberlearn.plataforma.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {
    @Autowired private ModuloRepository repo;

    @GetMapping
    public List<Modulo> listar() { return repo.findAll(); }

    @PutMapping("/{id}")
    public Modulo editar(@PathVariable long id, @RequestBody Modulo novo) {
        Modulo m = repo.findById(id).orElseThrow();
        m.setTitulo(novo.getTitulo());
        m.setConteudo(novo.getConteudo());
        return repo.save(m);
    }
}