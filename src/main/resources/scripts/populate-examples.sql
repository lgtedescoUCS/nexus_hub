-- Usuários
INSERT INTO USUARIO (nome, email, senha)
VALUES ('Ana Souza', 'ana.souza@example.com', 'senha123'),
       ('Bruno Lima', 'bruno.lima@example.com', 'senha456'),
       ('Carla Menezes', 'carla.m@example.com', 'abc123'),
       ('Diego Rocha', 'diego.rocha@example.com', 'pass789');

-- Categorias
INSERT INTO CATEGORIA (nome_categoria)
VALUES ('Estudos'),
       ('Trabalho'),
       ('Pessoal'),
       ('Saúde');

-- Matérias
INSERT INTO MATERIA (id_usuario, nome_materia, nome_professor)
VALUES (1, 'Banco de Dados', 'Prof. Ricardo Alves'),
       (1, 'Programação Web', 'Profa. Juliana Martins'),
       (2, 'Engenharia de Software', 'Prof. Caio Silva'),
       (3, 'Matemática Discreta', 'Profa. Lúcia Prado');

-- Tarefas
INSERT INTO TAREFA (id_usuario, descricao, data_criacao, concluida, prioridade, id_categoria)
VALUES (1, 'Estudar comandos SQL para prova', '2025-10-01 10:00:00', FALSE, 'alta', 1),
       (1, 'Enviar relatório semanal', '2025-10-02 09:30:00', TRUE, 'media', 2),
       (2, 'Organizar pastas do projeto final', '2025-10-03 14:00:00', FALSE, 'baixa', 2),
       (3, 'Ler capítulo sobre lógica matemática', '2025-10-04 08:00:00', FALSE, 'media', 1),
       (4, 'Agendar check-up médico', '2025-10-05 11:00:00', TRUE, 'baixa', 4);

-- Compromissos
INSERT INTO COMPROMISSO (id_usuario, titulo, data_hora_inicio, data_hora_fim, local, id_materia)
VALUES (1, 'Aula de Banco de Dados', '2025-10-08 08:00:00', '2025-10-08 10:00:00', 'Sala 204', 1),
       (1, 'Reunião de Projeto Web', '2025-10-09 15:00:00', '2025-10-09 16:30:00', 'Google Meet', 2),
       (2, 'Apresentação de Engenharia de Software', '2025-10-10 09:00:00', '2025-10-10 10:30:00', 'Auditório 3', 3),
       (3, 'Plantão de Dúvidas - Matemática Discreta', '2025-10-11 14:00:00', '2025-10-11 15:00:00', 'Sala 105', 4);

-- Lembretes
INSERT INTO LEMBRETE (mensagem, data_envio, id_tarefa, id_compromisso)
VALUES ('Não esquecer de revisar JOINs antes da aula!', '2025-10-07 18:00:00', 1, 1),
       ('Relatório já foi enviado?', '2025-10-03 08:00:00', 2, NULL),
       ('Preparar slides da apresentação', '2025-10-09 20:00:00', NULL, 3),
       ('Levar lista de exercícios resolvida', '2025-10-10 10:00:00', 4, 4);

-- Projetos
INSERT INTO PROJETO (id_usuario, nome_projeto, descricao)
VALUES (1, 'Sistema Acadêmico Nexus', 'Desenvolvimento de uma plataforma de gerenciamento de estudos.'),
       (2, 'Aplicativo de Tarefas Inteligente', 'App com notificações baseadas em geolocalização.'),
       (3, 'Portal de Exercícios Matemáticos', 'Plataforma colaborativa de resolução de problemas.'),
       (4, 'Dashboard de Saúde Pessoal', 'Ferramenta para monitoramento de hábitos e check-ups.');

-- Metas
INSERT INTO META (id_usuario, objetivo, prazo, progresso)
VALUES (1, 'Concluir o projeto Nexus antes de novembro', '2025-11-01 23:59:59', 60),
       (2, 'Atingir média 9 em Engenharia de Software', '2025-12-10 23:59:59', 40),
       (3, 'Ler 3 livros sobre lógica e teoria dos grafos', '2025-12-20 23:59:59', 20),
       (4, 'Melhorar alimentação e treinar 3x por semana', '2025-11-30 23:59:59', 75);