CREATE DATABASE  db_estoque;
USE db_estoque;
/*
drop database db_estoque;

 describe produto;
 describe categoria;
 describe 
 select * from produto;
 select * from categoria ;
*/

drop table produto;


CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    unidade VARCHAR(20) NOT NULL,
    quantidade INT NOT NULL,
    quantidade_minima INT NOT NULL,
    quantidade_maxima INT NOT NULL,
    categoria_id INT  
);

ALTER TABLE produto 
ADD CONSTRAINT fk_produto_categoria 
FOREIGN KEY (categoria_id) 
REFERENCES categoria(id);

drop table categoria;
CREATE TABLE  categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    tamanho ENUM('Pequeno', 'Médio', 'Grande') NOT NULL DEFAULT 'Médio',/* Aceita apenas 3 opçoes pre estabelecidas */
    embalagem ENUM('Lata', 'Plástico', 'Vidro') NOT NULL
) ENGINE=InnoDB;

