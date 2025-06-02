package DAO;

import Model.Produto;
import Model.Categoria;
import Model.Relatorio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public ProdutoDAO() {
    }
    // Retorna o maior ID de produto

    public int maiorID() throws SQLException {
        String sql = "SELECT MAX(id) FROM produto";
        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Insere um novo produto no banco
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco_unitario, unidade, quantidade, quantidade_minima, quantidade_maxima, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria() != null ? produto.getCategoria().getId() : 0);

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

    // Atualiza os dados de um produto existente
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome=?, preco_unitario=?, unidade=?, quantidade=?, quantidade_minima=?, quantidade_maxima=?, categoria_id=? WHERE id=?";

        try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria() != null ? produto.getCategoria().getId() : 0);
            stmt.setInt(8, produto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
        return false;
    }

    // Remove um produto pelo ID
    public boolean deletar(int id) {
        String sql = "DELETE FROM produto WHERE id=?";

        try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
        }
        return false;
    }

    // Busca um produto pelo ID
    public Produto buscarPorId(int id) {
        String sql = "SELECT p.*, c.nome as categoria_nome, c.tamanho, c.embalagem FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id WHERE p.id=?";

        try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

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

    // Lista todos os produtos cadastrados
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as categoria_nome, c.tamanho, c.embalagem FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id ORDER BY p.nome";

        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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

    // Atualiza a quantidade em estoque de um produto
    public boolean atualizarQuantidade(int id, int quantidadeVariacao) {
        if (quantidadeVariacao < 0) {
            String sqlVerificaEstoque = "SELECT quantidade FROM produto WHERE id = ?";
            try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmtVerifica = conexao.prepareStatement(sqlVerificaEstoque)) {

                stmtVerifica.setInt(1, id);
                try (ResultSet rs = stmtVerifica.executeQuery()) {
                    if (rs.next()) {
                        int estoqueAtual = rs.getInt("quantidade");
                        if (estoqueAtual + quantidadeVariacao < 0) {
                            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao verificar estoque: " + e.getMessage());
                return false;
            }
        }

        String sqlAtualiza = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";
        try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmtAtualiza = conexao.prepareStatement(sqlAtualiza)) {

            stmtAtualiza.setInt(1, quantidadeVariacao);
            stmtAtualiza.setInt(2, id);

            return stmtAtualiza.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar quantidade: " + e.getMessage());
        }
        return false;
    }

    // Lista produtos com estoque abaixo do mínimo
    public List<Produto> listarAbaixoDoMinimo() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as categoria_nome FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id WHERE p.quantidade < p.quantidade_minima ORDER BY p.nome";

        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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

    // Lista produtos com estoque acima do máximo
    public List<Produto> listarAcimaDoMaximo() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as categoria_nome FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id WHERE p.quantidade > p.quantidade_maxima ORDER BY p.nome";

        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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

    // Gera relatório de lista de preços
    public List<Relatorio> gerarListaPrecos() {
        List<Relatorio> relatorio = new ArrayList<>();
        String sql = "SELECT p.nome, p.preco_unitario, p.unidade, c.nome as categoria FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id ORDER BY p.nome";

        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                relatorio.add(new Relatorio(
                        rs.getString("nome"),
                        rs.getDouble("preco_unitario"),
                        rs.getString("unidade"),
                        rs.getString("categoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar lista de preços: " + e.getMessage());
        }
        return relatorio;
    }

    // Gera relatório de balanço financeiro
    public List<Relatorio> gerarBalanco() {
        List<Relatorio> relatorio = new ArrayList<>();
        String sql = "SELECT p.nome, p.quantidade, p.preco_unitario, (p.quantidade * p.preco_unitario) as valor_total "
                + "FROM produto p WHERE p.preco_unitario > 0 ORDER BY p.nome"; // Filtro para preços > 0

        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Dados do balanço:");
            while (rs.next()) {
                String nome = rs.getString("nome");
                int quantidade = rs.getInt("quantidade");
                double preco = rs.getDouble("preco_unitario");
                double total = rs.getDouble("valor_total");

                System.out.printf("%s - Qtd: %d - Preço: %.2f - Total: %.2f%n",
                        nome, quantidade, preco, total);

                relatorio.add(new Relatorio(nome, quantidade, preco, total));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar balanço: " + e.getMessage());
        }
        return relatorio;
    }

    // Gera relatório de produtos por categoria
    public List<Relatorio> gerarProdutosPorCategoria() {
        List<Relatorio> relatorio = new ArrayList<>();
        String sql = "SELECT c.nome as categoria, COUNT(p.id) as quantidade FROM categoria c LEFT JOIN produto p ON c.id = p.categoria_id GROUP BY c.nome ORDER BY c.nome";

        try (Connection conexao = ConnectionFactory.getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                relatorio.add(new Relatorio(
                        rs.getString("categoria"),
                        rs.getInt("quantidade")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar produtos por categoria: " + e.getMessage());
        }
        return relatorio;
    }

    // Aplica reajuste percentual em todos os preços
    public boolean reajustarPrecos(double percentual) {
        String sql = "UPDATE produto SET preco_unitario = preco_unitario * (1 + ? / 100)";

        try (Connection conexao = ConnectionFactory.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setDouble(1, percentual);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao reajustar preços: " + e.getMessage());
        }
        return false;
    }

    // Retorna lista de todos os produtos
    public ArrayList<Produto> getMinhaLista() {
        return new ArrayList<>(listarTodos());
    }
}
