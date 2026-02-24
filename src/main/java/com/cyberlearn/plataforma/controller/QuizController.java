package com.cyberlearn.plataforma.controller;

import com.cyberlearn.plataforma.model.Quiz;
import com.cyberlearn.plataforma.model.Pergunta; // Importante para a lógica de validação
import com.cyberlearn.plataforma.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Necessário para a resposta da API
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    // Listar todos os quizzes disponíveis
    @GetMapping
    public List<Quiz> listarQuizzes() {
        return quizRepository.findAll();
    }

    // Obter um quiz específico com as suas perguntas
    @GetMapping("/{id}")
    public Quiz obterQuiz(@PathVariable long id) { 
        // O uso de 'long' (minúsculo) resolve o erro de Null type safety
        return quizRepository.findById(id).orElseThrow();
    }

    // NOVO: Método para validar as respostas do aluno e calcular o XP
    @PostMapping("/{id}/submeter")
    public ResponseEntity<?> validarRespostas(@PathVariable long id, @RequestBody List<String> respostasUtilizador) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        List<Pergunta> perguntas = quiz.getPerguntas();
        int acertos = 0;

        // Compara a resposta enviada pelo frontend com a correta na DB
        for (int i = 0; i < perguntas.size(); i++) {
            if (i < respostasUtilizador.size()) {
                String respostaCorreta = perguntas.get(i).getRespostaCorreta();
                if (respostaCorreta.equalsIgnoreCase(respostasUtilizador.get(i))) {
                    acertos++;
                }
            }
        }

        double percentagem = ((double) acertos / perguntas.size()) * 100;
        
        // Retorna uma mensagem de sucesso com o desempenho
        return ResponseEntity.ok("Concluíste o quiz! Acertos: " + acertos + "/" + perguntas.size() + " (" + (int)percentagem + "%)");
    }
}