package a3.pkgfinal;

import VIEW.MenuPrincipal;

public class A3Final {

    public static void main(String[] args) {
        
        
        

        MenuPrincipal tela = new MenuPrincipal();
        tela.setVisible(true);
        
        
        
        
        
        /*ProdutoDAO testeTabela =  new ProdutoDAO();
        testeTabela.gerarBalanco();
        
        
        /*try {
            // Instanciando os DAOs
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            
            System.out.println("=== SISTEMA DE CONTROLE DE ESTOQUE ===");
            
            // 1. Cadastro de categorias
            System.out.println("\n1. Cadastrando categorias...");
            Categoria catBebidas = new Categoria("Bebidas", "Grande", "Plástico");
            Categoria catLimpeza = new Categoria("Limpeza", "Médio", "Plástico");
            Categoria catEnlatados = new Categoria("Enlatados", "Pequeno", "Lata");
            
            if(categoriaDAO.inserir(catBebidas) && 
               categoriaDAO.inserir(catLimpeza) && 
               categoriaDAO.inserir(catEnlatados)) {
                System.out.println("Categorias cadastradas com sucesso!");
            }
            
            // 2. Listar categorias
            System.out.println("\n2. Listando todas as categorias:");
            List<Categoria> categorias = categoriaDAO.listarTodos();
            categorias.forEach(System.out::println);
            
            // 3. Cadastro de produtos
            System.out.println("\n3. Cadastrando produtos...");
            Produto prod1 = new Produto("Refrigerante", 8.50, "Litro", 50, 10, 100, catBebidas);
            Produto prod2 = new Produto("Sabão em Pó", 12.90, "Kg", 25, 5, 40, catLimpeza);
            Produto prod3 = new Produto("Atum Enlatado", 6.80, "Unidade", 30, 15, 50, catEnlatados);
            
            if(produtoDAO.inserir(prod1) && 
               produtoDAO.inserir(prod2) && 
               produtoDAO.inserir(prod3)) {
                System.out.println("Produtos cadastrados com sucesso!");
            }
            
            
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } */
    }
}
