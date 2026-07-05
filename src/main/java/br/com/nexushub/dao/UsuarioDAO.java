package br.com.nexushub.dao;

import br.com.nexushub.factory.ConnectionFactory;
import br.com.nexushub.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    public Usuario login(String email, String senha) {
        String sql = "SELECT * FROM USUARIO WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                return usuario;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tentar fazer login: " + e.getMessage(), e);
        }

        return null;
    }

    public void cadastrar(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            stmt.execute();

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key")) {
                System.out.println("Erro: E-mail já cadastrado.");
            } else {
                throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage(), e);
            }
        }
    }
}
