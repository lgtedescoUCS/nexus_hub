package br.com.nexushub.dao;

import br.com.nexushub.factory.ConnectionFactory;
import br.com.nexushub.model.Compromisso;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompromissoDAO {

    public void adicionar(Compromisso compromisso) {
        String sql = "INSERT INTO COMPROMISSO (titulo, data_hora_inicio, data_hora_fim, local, id_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, compromisso.getTitulo());
            stmt.setTimestamp(2, Timestamp.valueOf(compromisso.getDataHoraInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(compromisso.getDataHoraFim()));
            stmt.setString(4, compromisso.getLocal());
            stmt.setInt(5, compromisso.getIdUsuario());

            stmt.execute();
            System.out.println("Compromisso agendado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao agendar compromisso: " + e.getMessage(), e);
        }
    }

    public List<Compromisso> listarPorUsuario(int idUsuario) {
        List<Compromisso> compromissos = new ArrayList<>();
        String sql = "SELECT * FROM COMPROMISSO WHERE id_usuario = ? ORDER BY data_hora_inicio";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Compromisso c = new Compromisso();
                c.setId(rs.getInt("id_compromisso"));
                c.setTitulo(rs.getString("titulo"));
                c.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                c.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
                c.setLocal(rs.getString("local"));
                c.setIdUsuario(rs.getInt("id_usuario"));

                compromissos.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar compromissos: " + e.getMessage(), e);
        }
        return compromissos;
    }

    public void atualizar(Compromisso compromisso) {
        String sql = "UPDATE COMPROMISSO SET titulo=?, data_hora_inicio=?, data_hora_fim=?, local=? WHERE id_compromisso=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, compromisso.getTitulo());
            stmt.setTimestamp(2, Timestamp.valueOf(compromisso.getDataHoraInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(compromisso.getDataHoraFim()));
            stmt.setString(4, compromisso.getLocal());
            stmt.setInt(5, compromisso.getId());

            stmt.executeUpdate();
            System.out.println("Compromisso atualizado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar compromisso: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM COMPROMISSO WHERE id_compromisso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Compromisso removido da agenda.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir compromisso: " + e.getMessage(), e);
        }
    }

    public Compromisso buscarPorId(int id) {
        String sql = "SELECT * FROM COMPROMISSO WHERE id_compromisso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Compromisso c = new Compromisso();
                c.setId(rs.getInt("id_compromisso"));
                c.setTitulo(rs.getString("titulo"));
                c.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                c.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
                c.setLocal(rs.getString("local"));
                c.setIdUsuario(rs.getInt("id_usuario"));
                return c;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compromisso: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Compromisso> buscarPorPalavraChave(int idUsuario, String palavraChave) {
        List<Compromisso> compromissos = new ArrayList<>();

        String sql = "SELECT * FROM COMPROMISSO WHERE id_usuario = ? AND (titulo ILIKE ? OR local ILIKE ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            String termoBusca = "%" + palavraChave + "%";
            stmt.setString(2, termoBusca);
            stmt.setString(3, termoBusca);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Compromisso c = new Compromisso();
                    c.setId(rs.getInt("id_compromisso"));
                    c.setTitulo(rs.getString("titulo"));

                    c.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                    c.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
                    c.setLocal(rs.getString("local"));
                    c.setIdUsuario(rs.getInt("id_usuario"));

                    compromissos.add(c);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compromissos: " + e.getMessage(), e);
        }
        return compromissos;
    }
}