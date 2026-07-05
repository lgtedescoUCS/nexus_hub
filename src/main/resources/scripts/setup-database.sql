-- 1. USUARIO (tabela base, referenciada por muitas)
CREATE TABLE USUARIO
(
    id_usuario SERIAL PRIMARY KEY,
    nome       VARCHAR(100)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    senha      VARCHAR(100)        NOT NULL
);

-- 2. CATEGORIA (referenciada por TAREFA)
CREATE TABLE CATEGORIA
(
    id_categoria   SERIAL PRIMARY KEY,
    nome_categoria VARCHAR(100) NOT NULL
);

-- 3. MATERIA (referenciada por COMPROMISSO)
CREATE TABLE MATERIA
(
    id_materia     SERIAL PRIMARY KEY,
    id_usuario     INTEGER      NOT NULL,
    nome_materia   VARCHAR(100) NOT NULL,
    nome_professor VARCHAR(100),
    FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario)
);

-- 4. TAREFA (depende de USUARIO e CATEGORIA)
CREATE TABLE TAREFA
(
    id_tarefa    SERIAL PRIMARY KEY,
    id_usuario   INTEGER   NOT NULL,
    descricao    TEXT      NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    concluida    BOOLEAN DEFAULT FALSE,
    prioridade   VARCHAR(20) CHECK (prioridade IN ('baixa', 'media', 'alta')),
    id_categoria INTEGER,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario),
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA (id_categoria)
);

-- 5. COMPROMISSO (depende de USUARIO e MATERIA)
CREATE TABLE COMPROMISSO
(
    id_compromisso   SERIAL PRIMARY KEY,
    id_usuario       INTEGER      NOT NULL,
    titulo           VARCHAR(100) NOT NULL,
    data_hora_inicio TIMESTAMP    NOT NULL,
    data_hora_fim    TIMESTAMP,
    local            VARCHAR(100),
    id_materia       INTEGER,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario),
    FOREIGN KEY (id_materia) REFERENCES MATERIA (id_materia)
);

-- 6. LEMBRETE (depende de TAREFA e COMPROMISSO)
CREATE TABLE LEMBRETE
(
    id_lembrete    SERIAL PRIMARY KEY,
    mensagem       TEXT      NOT NULL,
    data_envio     TIMESTAMP NOT NULL,
    id_tarefa      INTEGER,
    id_compromisso INTEGER,
    FOREIGN KEY (id_tarefa) REFERENCES TAREFA (id_tarefa),
    FOREIGN KEY (id_compromisso) REFERENCES COMPROMISSO (id_compromisso)
);

-- 7. PROJETO (depende apenas de USUARIO)
CREATE TABLE PROJETO
(
    id_projeto   SERIAL PRIMARY KEY,
    id_usuario   INTEGER      NOT NULL,
    nome_projeto VARCHAR(100) NOT NULL,
    descricao    TEXT,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario)
);

-- 8. META (depende apenas de USUARIO)
CREATE TABLE META
(
    id_meta    SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    objetivo   TEXT    NOT NULL,
    prazo      TIMESTAMP,
    progresso  INTEGER CHECK (progresso >= 0 AND progresso <= 100),
    FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario)
);