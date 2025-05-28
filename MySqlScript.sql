CREATE DATABASE IF NOT EXISTS db_estoque;
USE db_estoque;

/*
 describe produto;
 describe categoria;
 describe 
 select * from produto;
 select * from categoria ;
*/




CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tamanho VARCHAR(50),
    embalagem VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    unidade VARCHAR(20) NOT NULL,
    quantidade INT NOT NULL,
    quantidade_minima INT NOT NULL,
    quantidade_maxima INT NOT NULL,
    categoria_id INT  -- Coluna sem FK direta
);

ALTER TABLE produto 
ADD CONSTRAINT fk_produto_categoria 
FOREIGN KEY (categoria_id) 
REFERENCES categoria(id)

