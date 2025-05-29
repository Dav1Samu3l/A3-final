# Sistema de Controle de Estoque

Este projeto é um sistema desktop para gerenciamento de estoque, desenvolvido em Java com Swing e integração com banco de dados MySQL.

## Funcionalidades

- **Cadastro de Categorias:** Adicione, edite e remova categorias de produtos, incluindo informações de nome, tamanho e embalagem.
- **Cadastro de Produtos:** Gerencie produtos com nome, preço unitário, unidade, quantidade, quantidade mínima/máxima e categoria associada.
- **Movimentação de Estoque:** Realize entradas e saídas de produtos, com alertas para estoques abaixo do mínimo ou acima do máximo.
- **Relatórios:** Visualize relatórios de lista de preços, balanço de estoque, produtos por categoria, produtos abaixo do mínimo e acima do máximo.
- **Interface Gráfica Amigável:** Utiliza Java Swing para uma experiência intuitiva.

## Estrutura do Projeto

```
src/
  a3/pkgfinal/A3Final.java          
  DAO/                              
    CategoriaDAO.java
    ProdutoDAO.java
    ConnectionFactory.java
  Model/                          
    Categoria.java
    Produto.java
    Relatorio.java
  VIEW/                            
    CategoriaView.java
    ProdutoView.java
    RelatorioView.java
    MenuPrincipal.java
```

## Pré-requisitos

- Java 8 ou superior
- MySQL Server
- IDE recomendada: NetBeans ou VS Code

## Configuração do Banco de Dados

1. Crie o banco de dados `db_estoque` no MySQL.
2. Execute os scripts SQL para criar as tabelas `categoria` e `produto`.
3. Ajuste as credenciais de acesso ao banco em [`DAO/ConnectionFactory.java`](src/DAO/ConnectionFactory.java) se necessário.

## Como Executar

1. Compile o projeto em sua IDE ou via terminal.
2. Execute a classe principal [`A3Final.java`](src/a3/pkgfinal/A3Final.java).
3. A interface gráfica será aberta para uso.

## Observações

- O sistema utiliza JDBC puro para conexão com o banco.
- O código segue o padrão MVC para melhor organização.
- Relatórios são exibidos em abas na tela de relatórios.

## Licença

Este projeto é acadêmico e livre para uso e modificação.

---

