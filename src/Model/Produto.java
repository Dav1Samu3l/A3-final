package Model;

import DAO.ProdutoDAO;
import java.sql.SQLException;
import java.util.ArrayList;

public class Produto {

    private int id;
    private String nome;
    private double precoUnitario;
    private String unidade;
    private int quantidade;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private Categoria categoria;
    private final ProdutoDAO dao;

    public Produto() {
        this.dao = new ProdutoDAO();
    }

    public Produto(String nome, double precoUnitario, String unidade, int quantidade,
            int quantidadeMinima, int quantidadeMaxima, Categoria categoria) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.categoria = categoria;
        this.dao = new ProdutoDAO();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", nome=" + nome + ", precoUnitario=" + precoUnitario
                + ", unidade=" + unidade + ", quantidade=" + quantidade
                + ", quantidadeMinima=" + quantidadeMinima
                + ", quantidadeMaxima=" + quantidadeMaxima
                + ", categoria=" + categoria + '}';
    }

    // Métodos DAO
    public ArrayList<Produto> getMinhaLista() {
        return (ArrayList<Produto>) dao.listarTodos();
    }

    public boolean insertProdutoBD() throws SQLException {
        int id = this.dao.maiorID() + 1;
        this.setId(id);
        return this.dao.inserir(this);
    }

    public boolean deleteProdutoBD() {
        return this.dao.deletar(this.id);
    }

    public boolean updateProdutoBD() {
        return this.dao.atualizar(this);
    }

    public Produto carregaProduto(int id) {
        return this.dao.buscarPorId(id);
    }

    public int maiorID() throws SQLException {
        return this.dao.maiorID();
    }

    public boolean atualizarQuantidade(int quantidade) {
        return this.dao.atualizarQuantidade(this.id, quantidade);
    }

    public ArrayList<Produto> getProdutosAbaixoMinimo() {
        return (ArrayList<Produto>) this.dao.listarAbaixoDoMinimo();
    }

    public ArrayList<Produto> getProdutosAcimaMaximo() {
        return (ArrayList<Produto>) this.dao.listarAcimaDoMaximo();
    }
}
