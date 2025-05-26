package DAO;

import Model.Produto;
import Model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO() {
        this.conexao = ConnectionFactory.getConnection();
    }

    // Método para obter o maior ID
    public int maiorID() throws SQLException {
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM produto")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // CRUD - Create
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco_unitario, unidade, quantidade, "
                   + "quantidade_minima, quantidade_maxima, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria() != null ? produto.getCategoria().getId() : null);
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        }
        return false;
    }

    // CRUD - Update
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome=?, preco_unitario=?, unidade=?, quantidade=?, "
                   + "quantidade_minima=?, quantidade_maxima=?, categoria_id=? WHERE id=?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria() != null ? produto.getCategoria().getId() : null);
            stmt.setInt(8, produto.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
        return false;
    }

    // CRUD - Delete
    public boolean deletar(int id) {
        String sql = "DELETE FROM produto WHERE id=?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
        }
        return false;
    }

    // CRUD - Read (por ID)
    public Produto buscarPorId(int id) {
        String sql = "SELECT p.*, c.nome as categoria_nome, c.tamanho, c.embalagem "
                   + "FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id "
                   + "WHERE p.id=?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                    produto.setUnidade(rs.getString("unidade"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                    produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                    
                    if (rs.getInt("categoria_id") > 0) {
                        Categoria categoria = new Categoria();
                        categoria.setId(rs.getInt("categoria_id"));
                        categoria.setNome(rs.getString("categoria_nome"));
                        categoria.setTamanho(rs.getString("tamanho"));
                        categoria.setEmbalagem(rs.getString("embalagem"));
                        produto.setCategoria(categoria);
                    }
                    
                    return produto;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
        return null;
    }

    // CRUD - Read (todos)
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as categoria_nome, c.tamanho, c.embalagem "
                   + "FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id "
                   + "ORDER BY p.nome";
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                
                if (rs.getInt("categoria_id") > 0) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("categoria_id"));
                    categoria.setNome(rs.getString("categoria_nome"));
                    categoria.setTamanho(rs.getString("tamanho"));
                    categoria.setEmbalagem(rs.getString("embalagem"));
                    produto.setCategoria(categoria);
                }
                
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    // Métodos específicos para estoque
    public boolean atualizarQuantidade(int produtoId, int quantidade) {
        String sql = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar quantidade: " + e.getMessage());
        }
        return false;
    }

    public List<Produto> listarAbaixoDoMinimo() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as categoria_nome FROM produto p "
                   + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                   + "WHERE p.quantidade < p.quantidade_minima ORDER BY p.nome";
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                
                Categoria categoria = new Categoria();
                categoria.setNome(rs.getString("categoria_nome"));
                produto.setCategoria(categoria);
                
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos abaixo do mínimo: " + e.getMessage());
        }
        return produtos;
    }

    public List<Produto> listarAcimaDoMaximo() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as categoria_nome FROM produto p "
                   + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                   + "WHERE p.quantidade > p.quantidade_maxima ORDER BY p.nome";
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                
                Categoria categoria = new Categoria();
                categoria.setNome(rs.getString("categoria_nome"));
                produto.setCategoria(categoria);
                
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos acima do máximo: " + e.getMessage());
        }
        return produtos;
    }

    // Relatório de produtos por categoria
    public List<Object[]> relatorioProdutosPorCategoria() {
        List<Object[]> relatorio = new ArrayList<>();
        String sql = "SELECT c.nome as categoria, COUNT(p.id) as quantidade "
                   + "FROM categoria c LEFT JOIN produto p ON c.id = p.categoria_id "
                   + "GROUP BY c.nome ORDER BY c.nome";
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] linha = new Object[2];
                linha[0] = rs.getString("categoria");
                linha[1] = rs.getInt("quantidade");
                relatorio.add(linha);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
        return relatorio;
    }

    // Reajuste de preços
    public boolean reajustarPrecos(double percentual) {
        String sql = "UPDATE produto SET preco_unitario = preco_unitario * (1 + ? / 100)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDouble(1, percentual);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao reajustar preços: " + e.getMessage());
        }
        return false;
    }
}