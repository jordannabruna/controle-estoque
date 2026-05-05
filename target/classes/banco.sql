-- Criar banco de dados (comentado pois deve ser criado manualmente)
-- CREATE DATABASE controle_estoque_prog2_jordanna ENCODING 'UTF8';
-- Para criar: psql -U postgres -c "CREATE DATABASE controle_estoque_prog2_jordanna ENCODING 'UTF8';"

-- categoria
CREATE TABLE IF NOT EXISTS categoria (
    id   SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- produto
-- cada produto pertence a uma categoria
CREATE TABLE IF NOT EXISTS produto (
    id                  SERIAL PRIMARY KEY,
    nome                VARCHAR(150) NOT NULL,
    preco_medio         DOUBLE PRECISION NOT NULL DEFAULT 0,
    qtde_estoque        DOUBLE PRECISION NOT NULL DEFAULT 0,
    valor_ultima_compra DOUBLE PRECISION NOT NULL DEFAULT 0,
    valor_ultima_venda  DOUBLE PRECISION NOT NULL DEFAULT 0,
    id_categoria        INT NOT NULL,
    CONSTRAINT fk_produto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria (id)
);

-- cliente
CREATE TABLE IF NOT EXISTS cliente (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(150) NOT NULL,
    cpf       VARCHAR(14)  NOT NULL UNIQUE,
    rg        VARCHAR(20),
    endereco  VARCHAR(255),
    telefone  VARCHAR(20)
);

-- fornecedor
CREATE TABLE IF NOT EXISTS fornecedor (
    id             SERIAL PRIMARY KEY,
    nome_fantasia  VARCHAR(150) NOT NULL,
    razao_social   VARCHAR(200) NOT NULL,
    cnpj           VARCHAR(18)  NOT NULL UNIQUE
);

-- fornecedor_produto
-- relaciona fornecedores e produtos
CREATE TABLE IF NOT EXISTS fornecedor_produto (
    id             SERIAL PRIMARY KEY,
    id_fornecedor   INT NOT NULL,
    id_produto      INT NOT NULL,
    CONSTRAINT fk_fornecedor_produto_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES fornecedor (id),
    CONSTRAINT fk_fornecedor_produto_produto FOREIGN KEY (id_produto) REFERENCES produto (id),
    CONSTRAINT uk_fornecedor_produto UNIQUE (id_fornecedor, id_produto)
);

-- venda
-- cada venda pertence a um cliente
CREATE TABLE IF NOT EXISTS venda (
    id           SERIAL PRIMARY KEY,
    data_venda   DATE NOT NULL,
    valor_total  DOUBLE PRECISION NOT NULL DEFAULT 0,
    id_cliente   INT NOT NULL,
    CONSTRAINT fk_venda_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id)
);

-- item_venda
-- representa os produtos de cada venda
CREATE TABLE IF NOT EXISTS item_venda (
    id          SERIAL PRIMARY KEY,
    id_venda    INT NOT NULL,
    id_produto  INT NOT NULL,
    quantidade  DOUBLE PRECISION NOT NULL,
    valor_unit  DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_item_venda_venda   FOREIGN KEY (id_venda)   REFERENCES venda   (id),
    CONSTRAINT fk_item_venda_produto FOREIGN KEY (id_produto) REFERENCES produto  (id)
);

-- compra
-- cada compra pertence a um fornecedor
CREATE TABLE IF NOT EXISTS compra (
    id            SERIAL PRIMARY KEY,
    data_compra   DATE NOT NULL,
    valor_total   DOUBLE PRECISION NOT NULL DEFAULT 0,
    id_fornecedor INT NOT NULL,
    CONSTRAINT fk_compra_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES fornecedor (id)
);

-- item_compra
-- representa os produtos de cada compra
CREATE TABLE IF NOT EXISTS item_compra (
    id          SERIAL PRIMARY KEY,
    id_compra   INT NOT NULL,
    id_produto  INT NOT NULL,
    quantidade  DOUBLE PRECISION NOT NULL,
    valor_unit  DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_item_compra_compra  FOREIGN KEY (id_compra)  REFERENCES compra  (id),
    CONSTRAINT fk_item_compra_produto FOREIGN KEY (id_produto) REFERENCES produto (id)
);
