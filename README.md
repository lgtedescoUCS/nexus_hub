# Nexus Hub

Aplicação desktop em Java para gestão unificada de produtividade pessoal e acadêmica — tarefas, compromissos, matérias, projetos e metas em um único ambiente, com persistência em PostgreSQL.

![Java](https://img.shields.io/badge/Java-17%20|%2021-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Maven](https://img.shields.io/badge/build-Maven-red)

---

## O problema

Estudantes e profissionais espalham as próprias responsabilidades entre calendário físico, bloco de notas digital e planilha. A fragmentação custa dados perdidos, compromissos esquecidos e retrabalho.

O Nexus Hub centraliza tudo em uma aplicação só, com dados persistidos em banco relacional — não em arquivo local que some quando a aplicação fecha.

---

## O que o sistema faz

**Autenticação**
Login obrigatório por e-mail e senha. Cada usuário enxerga exclusivamente os próprios dados — o isolamento é garantido por chave estrangeira no schema, não por filtro na camada de apresentação.

**Gestão de tarefas**
CRUD completo com prioridade (alta, média, baixa), status de conclusão, data de criação e vínculo opcional a uma categoria.

**Gestão de compromissos**
Agendamento com título, local, início e fim. Aplica validação cronológica: o sistema recusa o cadastro de um término anterior ao início. Compromissos podem ser vinculados a uma matéria.

**Organização acadêmica**
Matérias com nome de disciplina e professor, vinculáveis aos compromissos do usuário.

**Planejamento**
Projetos agrupam atividades sob objetivos comuns. Metas registram objetivo, prazo e progresso percentual.

---

## Arquitetura

Três camadas desacopladas, inspiradas em MVC:

```
┌─────────────────────────────────────────┐
│  View (Console)                         │  Menus, entrada e saída via Scanner
│  br.com.nexushub.app                    │  Sem regra de negócio
├─────────────────────────────────────────┤
│  Model (Entidades + regras)             │  Usuario, Tarefa, Compromisso, Materia,
│                                         │  Projeto, Meta, Lembrete, Categoria
├─────────────────────────────────────────┤
│  DAO (Acesso a dados)                   │  Encapsula todo o SQL
│  br.com.nexushub.factory                │  ConnectionFactory centraliza conexões
├─────────────────────────────────────────┤
│  PostgreSQL                             │
└─────────────────────────────────────────┘
```

**Por que assim.** A camada de apresentação não conhece SQL e o DAO não conhece a interface. Consequência prática: migrar o console para JavaFX ou Swing não exige tocar em uma linha de regra de negócio ou de persistência.

A interface em console foi decisão deliberada desta etapa — isolar a complexidade visual para validar a lógica de negócio e a integridade das operações de banco primeiro.

**Padrões aplicados**

| Padrão | Onde | Por quê |
|---|---|---|
| DAO | Camada de persistência | Centraliza o SQL, evita repetição, permite trocar de SGBD sem tocar no negócio |
| Factory | `ConnectionFactory` | Cria conexões de forma centralizada e padronizada |

---

## Modelo de dados

Modelo relacional normalizado, com integridade referencial por chaves estrangeiras — não existe tarefa sem usuário associado.

| Tabela | Conteúdo | Vínculos |
|---|---|---|
| `usuario` | Nome, e-mail (único), senha | — |
| `tarefa` | Descrição, prioridade, conclusão, data de criação | usuário, categoria |
| `compromisso` | Título, local, início e fim | usuário, matéria |
| `materia` | Nome da disciplina e do professor | usuário |
| `categoria` | Classificação das tarefas | — |
| `projeto` | Nome e descrição | usuário |
| `meta` | Objetivo, prazo, progresso | usuário |
| `lembrete` | Mensagem e data de envio | tarefa e/ou compromisso |

O mapeamento objeto-relacional é manual via JDBC — cada atributo das entidades corresponde a uma coluna, sem ORM.

Schema em `src/main/resources/scripts/setup-database.sql`.

---

## Stack

| Componente | Tecnologia |
|---|---|
| Linguagem | Java 17 ou 21 |
| Build e dependências | Maven 3.9+ |
| Banco de dados | PostgreSQL |
| Conectividade | JDBC |
| Interface | Console / Terminal |

---

## Status

Entregue nesta etapa:

- [x] Autenticação de usuário
- [x] CRUD de tarefas com prioridade e status
- [x] CRUD de compromissos com validação cronológica
- [x] Persistência completa em PostgreSQL
- [x] Arquitetura em camadas com DAO e Factory

Modelado no schema, implementação em andamento:

- [ ] Modo Estudante completo, com relatórios de desempenho acadêmico
- [ ] Dashboard com resumo de pendências
- [ ] Hash de senha (atualmente armazenada em texto plano — correção prioritária)
- [ ] Migração da interface para JavaFX

---

## Como executar

**Pré-requisitos:** Git, JDK 17 ou 21, Maven 3.9+, PostgreSQL.

```bash
git clone https://github.com/lgtedescoUCS/nexus_hub.git
cd nexus_hub
```

Crie o banco e aplique o schema:

```bash
createdb nexushub
psql -d nexushub -f src/main/resources/scripts/setup-database.sql
```

Dados de exemplo, opcional:

```bash
psql -d nexushub -f src/main/resources/scripts/populate-examples.sql
```

Configure as credenciais em `src/main/java/br/com/nexushub/factory/ConnectionFactory.java`.

**Executar pela IDE:** rode `HubApplication.main()` em `br.com.nexushub.app`.

**Executar por linha de comando:** com o `exec-maven-plugin` no `pom.xml`,

```bash
mvn clean compile exec:java
```

---

## Contexto e créditos

Projeto desenvolvido na disciplina de Projeto Temático I — Engenharia de Software, Universidade de Caxias do Sul (UCS).

- **Luca Giasson Tedesco** — implementação: modelagem das entidades, camada DAO, regras de negócio, integração com PostgreSQL e interface de console.
- **Leandro Kaczalla Salvador** e **Luís Henrique Emer Dallegrave** — documentação técnica, diagrama de casos de uso e diagrama de classes.
