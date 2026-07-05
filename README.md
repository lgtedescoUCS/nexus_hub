# Nexus Hub

O Nexus Hub é uma aplicação desktop projetada para ser uma central de produtividade pessoal e acadêmica. A proposta é
criar uma plataforma unificada e inteligente que centraliza a gestão de tarefas, compromissos e metas em um único
ambiente coeso e de fácil utilização, construída em Java.

## 📋 Índice

- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Fluxo de Trabalho](#fluxo-de-trabalho)
- [Execução do Projeto](#execução-do-projeto)

## 🛠️ Pré-requisitos

Antes de iniciar, certifique-se de ter as seguintes ferramentas instaladas:

- [Git](https://git-scm.com/downloads)
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads) (versão 17 ou 21)
- [Apache Maven](https://maven.apache.org/download.cgi) (versão 3.9+)
- [PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads) (banco de dados)
- Uma IDE Java (recomendado: [IntelliJ IDEA](https://www.jetbrains.com/idea/download))

## ⚙️ Configuração do Ambiente

### 1. Clonando o Repositório

```bash
git clone https://github.com/lndroks/Nexus-Hub.git
cd Nexus-Hub
```

### 2. Configurando o Banco de Dados

#### a. Crie o Banco de Dados

Abra o `psql` ou uma ferramenta de sua preferência (como DBeaver ou PgAdmin) e execute o seguinte comando para criar a
base de dados:

```sql
CREATE
DATABASE nexushub;
```

#### b. Crie as Tabelas

Conecte-se à base de dados `nexushub` e execute o script SQL para criar todas as tabelas necessárias. O script está em
`src/main/resources/scripts/setup-database.sql`.

#### c. Configure a Conexão no Código

Abra o projeto na sua IDE e navegue até o arquivo:

```
src/main/java/br/com/nexushub/factory/ConnectionFactory.java
```

Altere as credenciais do banco de dados (usuário e senha) para as que você configurou localmente:

```java
String URL = "jdbc:postgresql://localhost:5432/nexushub";
String USER = "seu_usuario_aqui";
String PASSWORD = "sua_senha_aqui";
```

### 3. Instalando as Dependências

O projeto utiliza Maven para gerenciar as dependências. Ao importar o projeto na sua IDE, as dependências listadas no
arquivo `pom.xml` serão baixadas e configuradas automaticamente.

### 4. Populando o Banco com Dados de Exemplo (Opcional)

Para ter uma base de dados com usuários e atividades de exemplo, execute o script
`src/main/resources/scripts/populate-examples.sql` ou os comandos SQL abaixo diretamente no seu banco `nexushub`.

```sql
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
```

## 🔄 Fluxo de Trabalho

### Branches

- **main**: Branch principal, representa a versão estável (produção).
- **dev**: Branch de desenvolvimento. Novas funcionalidades são integradas aqui antes de irem para a main.
- **feature/nome-da-feature**: Padrão para novas funcionalidades. Ex: `feature/crud-tarefa`.

### Commits

Utilize mensagens claras e objetivas seguindo a convenção [Conventional Commits](https://www.conventionalcommits.org/):

- `feat`: para novas funcionalidades.
- `fix`: para correção de bugs.
- `docs`: para alterações na documentação.
- `style`: para formatação de código.
- `refactor`: para refatoração de código.
- `chore`: para tarefas de build, pacotes, etc.

### Pull Requests

- Crie o Pull Request a partir da sua branch de feature para a branch `dev`.
- Adicione uma descrição clara das alterações e, se possível, vincule a uma issue.

## 🚀 Execução do Projeto

### Via IDE (Recomendado)

1. Certifique-se de que a configuração do banco de dados está correta.
2. Encontre a classe `HubApplication.java` no caminho `src/main/java/br/com/nexushub/app`.
3. Clique com o botão direito sobre o arquivo e selecione **"Run 'HubApplication.main()'"**.

### Via Linha de Comando (Maven)

Para executar a aplicação de console via Maven, você precisa do `exec-maven-plugin`. Primeiro, garanta que ele está no
seu `pom.xml`, depois use o comando.

#### Passo 1: Adicione o plugin ao pom.xml (se não existir)

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <mainClass>br.com.nexushub.app.HubApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### Passo 2: Execute o comando

Navegue até o diretório raiz do projeto e execute:

```bash
mvn clean compile exec:java
```

---

Desenvolvido com ☕