package Model;

import DAO.CategoriaDAO;
import java.sql.SQLException;
import java.util.ArrayList;

public class Categoria {
    // Atributos
    private int id;
    private String nome;
    private String tamanho;
    private String embalagem;
    private final CategoriaDAO dao;

    public Categoria() {
        this.dao = new CategoriaDAO();
    }

    public Categoria(String nome, String tamanho, String embalagem) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
        this.dao = new CategoriaDAO();
    }

    public Categoria(int id, String nome, String tamanho, String embalagem) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
        this.dao = new CategoriaDAO();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
    public String getEmbalagem() { return embalagem; }
    public void setEmbalagem(String embalagem) { this.embalagem = embalagem; }

  /*  @Override
    public String toString() {
        return "Categoria{" + "id=" + id + ", nome=" + nome + 
               ", tamanho=" + tamanho + ", embalagem=" + embalagem + '}';
    }
*/
    // Métodos DAO
    public ArrayList<Categoria> getMinhaLista() {
        return (ArrayList<Categoria>) dao.listarTodos();
    }

    public boolean insertCategoriaBD() throws SQLException {
        int id = this.dao.maiorID() + 1;
        this.setId(id);
        return this.dao.inserir(this);
    }

    public boolean deleteCategoriaBD() {
        return this.dao.deletar(this.id);
    }

    public boolean updateCategoriaBD() {
        return this.dao.atualizar(this);
    }

    public Categoria carregaCategoria(int id) {
        return this.dao.buscarPorId(id);
    }

    public int maiorID() throws SQLException {
        return this.dao.maiorID();
    }
}