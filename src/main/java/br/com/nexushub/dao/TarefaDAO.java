package br.com.nexushub.dao;

import br.com.nexushub.factory.ConnectionFactory;
import br.com.nexushub.model.Tarefa;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    public void adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO TAREFA (descricao, data_criacao, concluida, prioridade, id_usuario, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getDescricao());
            stmt.setDate(2, Date.valueOf(tarefa.getDataCriacao()));
            stmt.setBoolean(3, tarefa.isConcluida());
            stmt.setString(4, tarefa.getPrioridade());
            stmt.setInt(5, tarefa.getIdUsuario());
            stmt.setInt(6, tarefa.getIdCategoria());

            stmt.execute();

            System.out.println("Tarefa adicionada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar tarefa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarTarefa(Tarefa tarefa) {
        String sql = "UPDATE TAREFA SET descricao = ?, concluida = ?, prioridade = ? WHERE id_tarefa = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getDescricao());
            stmt.setBoolean(2, tarefa.isConcluida());
            stmt.setString(3, tarefa.getPrioridade());
            stmt.setInt(4, tarefa.getIdTarefa());

            stmt.executeUpdate();
            System.out.println("Tarefa atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void excluirTarefa(int id) {
        String sql = "DELETE FROM TAREFA WHERE id_tarefa = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
            System.out.println("Tarefa excluída com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir tarefa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Tarefa> listarPorUsuario(int idUsuario) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM TAREFA WHERE id_usuario = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario); // Define o filtro pelo ID do usuário

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String descricao = rs.getString("descricao");
                    LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
                    String prioridade = rs.getString("prioridade");
                    int idCategoria = rs.getInt("id_categoria");

                    Tarefa tarefa = new Tarefa(descricao, dataCriacao, prioridade, idUsuario, idCategoria);

                    tarefa.setIdTarefa(rs.getInt("id_tarefa"));
                    tarefa.setConcluida(rs.getBoolean("concluida"));

                    tarefas.add(tarefa);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas: " + e.getMessage(), e);
        }
        return tarefas;
    }

    public Tarefa buscarTarefaPorId(int id) {
        String sql = "SELECT * FROM TAREFA WHERE id_tarefa = ?";
        Tarefa tarefa = null;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String descricao = rs.getString("descricao");
                    LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
                    String prioridade = rs.getString("prioridade");
                    int idUsuario = rs.getInt("id_usuario");
                    int idCategoria = rs.getInt("id_categoria");

                    tarefa = new Tarefa(descricao, dataCriacao, prioridade, idUsuario, idCategoria);
                    tarefa.setIdTarefa(rs.getInt("id_tarefa"));
                    tarefa.setConcluida(rs.getBoolean("concluida"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefa por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return tarefa;
    }

    public List<Tarefa> buscarPorPalavraChave(int idUsuario, String palavraChave) {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM TAREFA WHERE id_usuario = ? AND descricao ILIKE ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            stmt.setString(2, "%" + palavraChave + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String descricao = rs.getString("descricao");
                    LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
                    String prioridade = rs.getString("prioridade");
                    int idCategoria = rs.getInt("id_categoria");

                    Tarefa tarefa = new Tarefa(descricao, dataCriacao, prioridade, idUsuario, idCategoria);
                    tarefa.setIdTarefa(rs.getInt("id_tarefa"));
                    tarefa.setConcluida(rs.getBoolean("concluida"));

                    tarefas.add(tarefa);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar tarefas: " + e.getMessage(), e);
        }
        return tarefas;
    }
}