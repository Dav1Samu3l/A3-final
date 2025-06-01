package Model;

public class Relatorio {

    private String nome;
    private double precoUnitario;
    private String unidade;
    private String categoria;
    private int quantidade;
    private double valorTotal;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private String nomeCategoria;
    private int quantidadeProdutos;

    public Relatorio() {
    }

    // Para Preços
    public Relatorio(String nome, double precoUnitario, String unidade, String categoria) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.unidade = unidade;
        this.categoria = categoria;
    }

    // Para Balanço
    public Relatorio(String nome, int quantidade, double precoUnitario, double valorTotal) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
    }

    // Para Produtos abaixo do mínimo/acima do máximo
    public Relatorio(String nome, int quantidade, int quantidadeMinima, int quantidadeMaxima) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
    }

    // Para Produtos por Categoria
    public Relatorio(String nomeCategoria, int quantidadeProdutos) {
        this.nomeCategoria = nomeCategoria;
        this.quantidadeProdutos = quantidadeProdutos;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public int getQuantidadeProdutos() {
        return quantidadeProdutos;
    }
}
