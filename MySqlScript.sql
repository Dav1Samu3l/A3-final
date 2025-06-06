        



        CREATE DATABASE IF NOT EXISTS db_estoque;

        USE db_estoque;

        CREATE TABLE IF NOT EXISTS produto (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(100) NOT NULL,
            preco_unitario DECIMAL(10, 2) NOT NULL,
            unidade VARCHAR(20) NOT NULL,
            quantidade INT NOT NULL,
            quantidade_minima INT NOT NULL,
            quantidade_maxima INT NOT NULL,
            categoria_id INT  
        )ENGINE=InnoDB;

        CREATE TABLE  IF NOT EXISTS categoria (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(100) NOT NULL,
            tamanho ENUM('Pequeno', 'Médio', 'Grande') NOT NULL DEFAULT 'Médio',
            embalagem ENUM('Lata', 'Plástico', 'Vidro') NOT NULL
        ) ENGINE=InnoDB;

        ALTER TABLE produto 
        ADD CONSTRAINT fk_produto_categoria 
        FOREIGN KEY (categoria_id) 
        REFERENCES categoria(id);





