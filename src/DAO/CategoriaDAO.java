package DAO;

import Model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public CategoriaDAO() {
    }

    // Obtém o maior ID existente na tabela
    public int maiorID() throws SQLException {
        String sql = "SELECT MAX(id) FROM categoria";
        try (Connection conexao = ConnectionFactory.getConnection();
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Insere nova categoria e captura ID gerado
    public boolean inserir(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, tamanho, embalagem)"
                + " VALUES (?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTamanho());
            stmt.setString(3, categoria.getEmbalagem());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
        }
        return false;
    }

    // Atualiza dados de categoria existente
    public boolean atualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nome=?, tamanho=?, embalagem=? WHERE id=?";

        try (Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTamanho());
            stmt.setString(3, categoria.getEmbalagem());
            stmt.setInt(4, categoria.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
        }
        return false;
    }

    // Remove categoria se não houver produtos vinculados
    public boolean deletar(int id) {

        // Verifica se existem produtos associados
        if (existemProdutosAssociados(id)) {
            return false;
        }

        String sql = "DELETE FROM categoria WHERE id=?";

        try (Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar categoria: " + e.getMessage());
        }
        return false;
    }

    // Verifica existência de produtos na categoria
    private boolean existemProdutosAssociados(int categoriaId) {
        String sql = "SELECT COUNT(*) FROM produto WHERE categoria_id = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, categoriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar produtos associados: " + e.getMessage());
        }
        return false;
    }

    // Busca categoria pelo ID
    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categoria WHERE id=?";

        try (Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("tamanho"),
                            rs.getString("embalagem"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }

    // Lista todas categorias como arrayList e ordenadas por nome
    public List<Categoria> listarTodos() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY nome";

        try (Connection conexao = ConnectionFactory.getConnection();
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tamanho"),
                        rs.getString("embalagem")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }

    // Retorna lista de categorias como ArrayList
    public ArrayList<Categoria> getMinhaLista() {
        return new ArrayList<>(listarTodos());
    }
}




