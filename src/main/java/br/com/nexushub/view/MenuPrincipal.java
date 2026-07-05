package br.com.nexushub.view;

import br.com.nexushub.dao.CompromissoDAO;
import br.com.nexushub.dao.TarefaDAO;
import br.com.nexushub.dao.UsuarioDAO;
import br.com.nexushub.model.Compromisso;
import br.com.nexushub.model.Tarefa;
import br.com.nexushub.model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuPrincipal {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TarefaDAO tarefaDAO = new TarefaDAO();
    private static final CompromissoDAO compromissoDAO = new CompromissoDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Usuario usuarioLogado;

    public void exibirMenu() {
        if (!fazerLogin()) {
            System.out.println("Login falhou. Encerrando aplicação.");
            return;
        }

        exibirDashboard();

        System.out.println("\nBem-vindo, " + usuarioLogado.getNome() + "!");

        while (true) {
            System.out.println("\n---[ Nexus Hub - Menu Principal ]---");
            System.out.println("1. Gerir Tarefas");
            System.out.println("2. Gerir Compromissos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    menuTarefas();
                    break;
                case 2:
                    menuCompromissos();
                    break;
                case 0:
                    System.out.println("A sair do sistema...");
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void exibirDashboard() {
        System.out.println("\n========================================");
        System.out.println("      DASHBOARD - " + usuarioLogado.getNome().toUpperCase());
        System.out.println("========================================");

        System.out.println("\n---[ Tarefas Prioritárias Pendentes ]---");
        List<Tarefa> todasTarefas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());

        List<Tarefa> pendentes = todasTarefas.stream()
                .filter(t -> !t.isConcluida())
                .limit(3)
                .collect(Collectors.toList());

        if (pendentes.isEmpty()) {
            System.out.println("Nenhuma tarefa pendente! Bom trabalho.");
        } else {
            for (Tarefa t : pendentes) {
                System.out.printf("- [%s] %s\n", t.getPrioridade().toUpperCase(), t.getDescricao());
            }
            long totalPendentes = todasTarefas.stream().filter(t -> !t.isConcluida()).count();
            if (totalPendentes > 3) {
                System.out.println("... e mais " + (totalPendentes - 3) + " tarefas.");
            }
        }

        System.out.println("\n---[ Próximos Compromissos ]---");
        List<Compromisso> todosCompromissos = compromissoDAO.listarPorUsuario(usuarioLogado.getId());

        LocalDateTime agora = LocalDateTime.now();
        List<Compromisso> futuros = todosCompromissos.stream()
                .filter(c -> c.getDataHoraFim().isAfter(agora))
                .sorted(Comparator.comparing(Compromisso::getDataHoraInicio))
                .limit(3)
                .collect(Collectors.toList());

        if (futuros.isEmpty()) {
            System.out.println("Nenhum compromisso agendado para breve.");
        } else {
            for (Compromisso c : futuros) {
                System.out.printf("- %s: %s (%s)\n",
                        c.getDataHoraInicio().format(formatter),
                        c.getTitulo(),
                        c.getLocal());
            }
        }
        System.out.println("========================================\n");

        System.out.println("Pressione Enter para ir ao menu...");
        scanner.nextLine();
    }

    private boolean fazerLogin() {
        while (true) {
            System.out.println("\n---[ Acesso ao Nexus Hub ]---");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar Novo Usuário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();

            if (opcao == 1) {
                System.out.print("E-mail: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                Usuario usuario = usuarioDAO.login(email, senha);

                if (usuario != null) {
                    this.usuarioLogado = usuario;
                    return true;
                } else {
                    System.out.println("Credenciais inválidas!");
                }
            } else if (opcao == 2) {
                cadastrarUsuario();
            } else if (opcao == 0) {
                return false;
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarUsuario() {
        System.out.println("\n--- Novo Cadastro ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return;
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);
        usuarioDAO.cadastrar(novoUsuario);

        System.out.println("Cadastro realizado com sucesso! Faça login para continuar.");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // --- MÉTODOS DE TAREFAS ---

    private void menuTarefas() {
        while (true) {
            System.out.println("\n---[ Gerir Tarefas ]---");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Todas as Tarefas");
            System.out.println("3. Buscar Tarefas (Palavra-chave)"); // Nova opção
            System.out.println("4. Atualizar Tarefa");
            System.out.println("5. Excluir Tarefa");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1: adicionarTarefa(); break;
                case 2: listarTarefas(); break;
                case 3: buscarTarefas(); break;
                case 4: atualizarTarefa(); break;
                case 5: excluirTarefa(); break;
                case 0: return;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private void adicionarTarefa() {
        System.out.println("\n--- Adicionar Nova Tarefa ---");
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        String prioridade = "";
        while (true) {
            System.out.print("Prioridade (baixa, media, alta): ");
            prioridade = scanner.nextLine().toLowerCase();
            if (prioridade.equals("baixa") || prioridade.equals("media") || prioridade.equals("alta")) {
                break;
            }
            System.out.println("Prioridade inválida!");
        }

        int idCategoria = 1;

        Tarefa tarefa = new Tarefa(descricao, LocalDate.now(), prioridade, usuarioLogado.getId(), idCategoria);
        tarefaDAO.adicionarTarefa(tarefa);
    }

    private void listarTarefas() {
        System.out.println("\n--- Lista de Tarefas de " + usuarioLogado.getNome() + " ---");

        List<Tarefa> tarefas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (Tarefa t : tarefas) {
                System.out.printf("ID: %d | Descrição: %s | Prioridade: %s | Concluída: %s\n",
                        t.getIdTarefa(),
                        t.getDescricao(),
                        t.getPrioridade(),
                        t.isConcluida() ? "Sim" : "Não");
            }
        }
    }

    private void buscarTarefas() {
        System.out.print("Digite a palavra-chave para buscar na descrição: ");
        String termo = scanner.nextLine();

        List<Tarefa> tarefas = tarefaDAO.buscarPorPalavraChave(usuarioLogado.getId(), termo);

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com o termo '" + termo + "'.");
        } else {
            System.out.println("\n--- Resultados da Busca ---");
            for (Tarefa t : tarefas) {
                System.out.printf("ID: %d | Descrição: %s | Prioridade: %s | Concluída: %s\n",
                        t.getIdTarefa(), t.getDescricao(), t.getPrioridade(), t.isConcluida() ? "Sim" : "Não");
            }
        }
    }

    private void atualizarTarefa() {
        listarTarefas();
        System.out.print("ID da tarefa a atualizar: ");
        int id = lerOpcao();

        Tarefa tarefa = tarefaDAO.buscarTarefaPorId(id);

        if (tarefa == null || tarefa.getIdUsuario() != usuarioLogado.getId()) {
            System.out.println("Tarefa não encontrada ou sem permissão.");
            return;
        }

        System.out.print("Nova descrição (Enter para manter '" + tarefa.getDescricao() + "'): ");
        String desc = scanner.nextLine();
        if (!desc.isEmpty()) tarefa.setDescricao(desc);

        tarefaDAO.atualizarTarefa(tarefa);
    }

    private void excluirTarefa() {
        listarTarefas();
        System.out.print("ID da tarefa a excluir: ");
        int id = lerOpcao();

        tarefaDAO.excluirTarefa(id);
    }


    // --- MÉTODOS DE COMPROMISSOS ---

    private void menuCompromissos() {
        while (true) {
            System.out.println("\n---[ Gerir Compromissos ]---");
            System.out.println("1. Agendar Compromisso");
            System.out.println("2. Listar Todos os Compromissos");
            System.out.println("3. Buscar Compromissos (Palavra-chave)"); // Nova opção
            System.out.println("4. Atualizar Compromisso");
            System.out.println("5. Excluir Compromisso");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1: adicionarCompromisso(); break;
                case 2: listarCompromissos(); break;
                case 3: buscarCompromissos(); break;
                case 4: atualizarCompromisso(); break;
                case 5: excluirCompromisso(); break;
                case 0: return;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private void adicionarCompromisso() {
        System.out.println("\n--- Agendar Novo Compromisso ---");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Local: ");
        String local = scanner.nextLine();

        LocalDateTime inicio = lerDataHora("Data/Hora Início (dd/MM/yyyy HH:mm): ");
        if (inicio == null) return;

        LocalDateTime fim = lerDataHora("Data/Hora Fim (dd/MM/yyyy HH:mm): ");
        if (fim == null) return;

        if (fim.isBefore(inicio)) {
            System.out.println("Erro: A data de fim não pode ser anterior à data de início.");
            return;
        }

        Compromisso compromisso = new Compromisso(titulo, inicio, fim, local, usuarioLogado.getId());
        compromissoDAO.adicionar(compromisso);
    }

    private void listarCompromissos() {
        System.out.println("\n--- Agenda de Compromissos de " + usuarioLogado.getNome() + " ---");

        List<Compromisso> lista = compromissoDAO.listarPorUsuario(usuarioLogado.getId());

        if (lista.isEmpty()) {
            System.out.println("Nenhum compromisso agendado.");
        } else {
            for (Compromisso c : lista) {
                System.out.printf("ID: %d | %s | %s - %s | %s\n",
                        c.getId(),
                        c.getTitulo(),
                        c.getDataHoraInicio().format(formatter),
                        c.getDataHoraFim().format(formatter),
                        c.getLocal());
            }
        }
    }

    private void buscarCompromissos() {
        System.out.print("Digite a palavra-chave para buscar (Título ou Local): ");
        String termo = scanner.nextLine();

        List<Compromisso> compromissos = compromissoDAO.buscarPorPalavraChave(usuarioLogado.getId(), termo);

        if (compromissos.isEmpty()) {
            System.out.println("Nenhum compromisso encontrado com o termo '" + termo + "'.");
        } else {
            System.out.println("\n--- Resultados da Busca ---");
            for (Compromisso c : compromissos) {
                System.out.printf("ID: %d | %s | %s - %s | %s\n",
                        c.getId(),
                        c.getTitulo(),
                        c.getDataHoraInicio().format(formatter),
                        c.getDataHoraFim().format(formatter),
                        c.getLocal());
            }
        }
    }

    private void atualizarCompromisso() {
        listarCompromissos();
        System.out.print("ID do compromisso a atualizar: ");
        int id = lerOpcao();

        Compromisso compromisso = compromissoDAO.buscarPorId(id);

        if (compromisso == null || compromisso.getIdUsuario() != usuarioLogado.getId()) {
            System.out.println("Compromisso não encontrado ou sem permissão.");
            return;
        }

        System.out.print("Novo título (Enter para manter '" + compromisso.getTitulo() + "'): ");
        String titulo = scanner.nextLine();
        if (!titulo.isEmpty()) compromisso.setTitulo(titulo);

        System.out.print("Novo local (Enter para manter '" + compromisso.getLocal() + "'): ");
        String local = scanner.nextLine();
        if (!local.isEmpty()) compromisso.setLocal(local);

        System.out.print("Deseja alterar as datas? (S/N): ");
        String alterarDatas = scanner.nextLine();

        if (alterarDatas.equalsIgnoreCase("S")) {
            LocalDateTime inicio = lerDataHora("Nova Data/Hora Início (dd/MM/yyyy HH:mm): ");
            if (inicio != null) {
                LocalDateTime fim = lerDataHora("Nova Data/Hora Fim (dd/MM/yyyy HH:mm): ");
                if (fim != null) {
                    if (fim.isBefore(inicio)) {
                        System.out.println("Erro: A data de fim não pode ser anterior à data de início. Datas não alteradas.");
                    } else {
                        compromisso.setDataHoraInicio(inicio);
                        compromisso.setDataHoraFim(fim);
                    }
                }
            }
        }

        compromissoDAO.atualizar(compromisso);
    }

    private void excluirCompromisso() {
        listarCompromissos();
        System.out.print("ID do compromisso a excluir: ");
        int id = lerOpcao();
        compromissoDAO.excluir(id);
    }

    private LocalDateTime lerDataHora(String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine();
        try {
            return LocalDateTime.parse(entrada, formatter);
        } catch (Exception e) {
            System.out.println("Formato de data inválido! Use dd/MM/yyyy HH:mm");
            return null;
        }
    }
}