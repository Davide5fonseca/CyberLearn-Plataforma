package com.cyberlearn.plataforma.controller;

import com.cyberlearn.plataforma.model.Quiz;
import com.cyberlearn.plataforma.model.Pergunta;
import com.cyberlearn.plataforma.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping
    public List<Quiz> listarQuizzes() {
        return quizRepository.findAll();
    }

    @GetMapping("/{id}")
    public Quiz obterQuiz(@PathVariable long id) { 
        // Resolve o "Null type safety" lançando uma exceção se não encontrar
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz não encontrado com o ID: " + id));
    }

    @PostMapping("/{id}/submeter")
    public ResponseEntity<?> validarRespostas(@PathVariable long id, @RequestBody List<String> respostasUtilizador) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz não encontrado"));
        
        List<Pergunta> perguntas = quiz.getPerguntas();
        int acertos = 0;

        for (int i = 0; i < perguntas.size(); i++) {
            if (i < respostasUtilizador.size()) {
                String respostaCorreta = perguntas.get(i).getRespostaCorreta();
                if (respostaCorreta.equalsIgnoreCase(respostasUtilizador.get(i))) {
                    acertos++;
                }
            }
        }

        double percentagem = ((double) acertos / perguntas.size()) * 100;
        return ResponseEntity.ok("Concluíste o quiz! Acertos: " + acertos + "/" + perguntas.size() + " (" + (int)percentagem + "%)");
    }

    // --- MÉTODOS PARA O PROFESSOR ---

    @PostMapping
    public Quiz criarQuiz(@RequestBody Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> editarQuiz(@PathVariable long id, @RequestBody Quiz quizAtualizado) {
        // Resolve o erro de getDescricao() e os avisos de Null safety
        return quizRepository.findById(id).map(quiz -> {
            quiz.setTitulo(quizAtualizado.getTitulo());
            quiz.setDescricao(quizAtualizado.getDescricao());
            
            if (quizAtualizado.getPerguntas() != null) {
                quiz.getPerguntas().clear();
                quiz.getPerguntas().addAll(quizAtualizado.getPerguntas());
            }
            
            return ResponseEntity.ok(quizRepository.save(quiz));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarQuiz(@PathVariable long id) {
        return quizRepository.findById(id).map(quiz -> {
            quizRepository.delete(quiz);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}