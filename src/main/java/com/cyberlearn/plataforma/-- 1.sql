-- 1. Criar o Quiz de Phishing vinculado ao Módulo 1
INSERT INTO quizzes (modulo_id, titulo, pontos_recompensa) 
VALUES (1, 'Desafio: Detetive de Phishing', 100);

-- 2. Inserir as perguntas (Exemplos das primeiras 3 para testares)
INSERT INTO perguntas (quiz_id, enunciado, opcao_a, opcao_b, opcao_c, opcao_d, resposta_correta) VALUES 
(1, 'Qual é o principal objetivo de um ataque de phishing?', 'Criptografar ficheiros', 'Enganar para obter dados', 'Aumentar velocidade', 'Testar firewalls', 'B'),
(1, 'O que distingue o Spear Phishing?', 'Apenas via redes sociais', 'Ataque em massa', 'Ataque direcionado', 'Usa apenas PDFs', 'C'),
(1, 'Receber um link falso por SMS chama-se:', 'Vishing', 'Smishing', 'Whaling', 'Pharming', 'B');