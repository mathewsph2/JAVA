use sibov;

-- Criação dos usuários administradores do Banco de Dados: 

-- Usuário 1

-- Criação do usuário e senha 
CREATE USER 'matheus'@'localhost' IDENTIFIED BY 'matheus';

-- Conceder todos os privilégios em todos os bancos de dados
GRANT ALL PRIVILEGES ON *.* TO 'matheus'@'localhost' WITH GRANT OPTION;

-- Atualizar privilégios
FLUSH PRIVILEGES;



-- Usuário 2 

-- Criação do usuário e senha 
CREATE USER 'kelmer' IDENTIFIED BY 'kelmer';

-- Conceder todos os privilégios em todos os bancos de dados
GRANT ALL PRIVILEGES ON *.* TO 'kelmer' WITH GRANT OPTION;

-- Atualizar privilégios
FLUSH PRIVILEGES;



-- Listar os Usuarios Criados: 

SELECT * FROM mysql.user; 





-- Isso muda o método de autenticação para um modo mais compatível com JDBC (antigo, mas seguro).
ALTER USER 'matheus'@'localhost' IDENTIFIED WITH mysql_native_password BY 'matheus';
FLUSH PRIVILEGES;



-- Criação e seleção da Base de Dados
CREATE DATABASE SIBOV;

USE SIBOV;

-- Criação da tabela dos Ativos: 
CREATE TABLE Ativo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ticker VARCHAR(20) NOT NULL,
    tipo ENUM('ACAO', 'FII') NOT NULL,
    segmento VARCHAR(100) NOT NULL,
    observacoes VARCHAR(300)
);

ALTER TABLE Ativo
ADD COLUMN valor_cota DECIMAL(10,2) NOT NULL DEFAULT 0.00 AFTER segmento;


-- Verificação se já há mais de um ticket com mesmo nome cadastrado antes da execução do próximo comando:
SELECT ticker, COUNT(*) 
FROM Ativo
GROUP BY ticker
HAVING COUNT(*) > 1;


-- Não permitir mais de um ticket com o mesmo nome 
ALTER TABLE Ativo
ADD CONSTRAINT unico_ticker UNIQUE (ticker);




-- FUNDOS IMOBILIÁRIOS (FII)
INSERT INTO Ativo (ticker, tipo, segmento, observacoes) VALUES
('HGLG11', 'FII', 'Logística', 'FII com foco em galpões logísticos e industriais.'),
('KNRI11', 'FII', 'Híbrido', 'Fundo misto com imóveis de escritórios e galpões.'),
('MXRF11', 'FII', 'Papel', 'Fundo de recebíveis imobiliários com boa liquidez.'),
('VISC11', 'FII', 'Shoppings', 'Fundo com participação em shoppings centers.'),
('BCFF11', 'FII', 'Fundo de Fundos', 'Investe em cotas de outros FIIs, diversificado.'),
('XPML11', 'FII', 'Shoppings', 'Fundo com portfólio de alto padrão em grandes capitais.'),
('RECT11', 'FII', 'Escritórios', 'Fundo voltado para lajes corporativas.'),
('DEVA11', 'FII', 'Papel', 'Fundo de CRIs com foco em renda fixa imobiliária.'),
('RBRP11', 'FII', 'Híbrido', 'Combina imóveis físicos e papéis, gestão ativa.'),
('PVBI11', 'FII', 'Escritórios', 'Fundo de lajes corporativas premium em São Paulo.');


UPDATE Ativo SET valor_cota = 175.20 WHERE ticker = 'HGLG11';
UPDATE Ativo SET valor_cota = 156.40 WHERE ticker = 'KNRI11';
UPDATE Ativo SET valor_cota = 10.20  WHERE ticker = 'MXRF11';
UPDATE Ativo SET valor_cota = 109.30 WHERE ticker = 'VISC11';
UPDATE Ativo SET valor_cota = 67.50  WHERE ticker = 'BCFF11';
UPDATE Ativo SET valor_cota = 103.10 WHERE ticker = 'XPML11';
UPDATE Ativo SET valor_cota = 48.80  WHERE ticker = 'RECT11';
UPDATE Ativo SET valor_cota = 90.10  WHERE ticker = 'DEVA11';
UPDATE Ativo SET valor_cota = 84.50  WHERE ticker = 'RBRP11';
UPDATE Ativo SET valor_cota = 94.70  WHERE ticker = 'PVBI11';

-- AÇÕES (ACAO)
INSERT INTO Ativo (ticker, tipo, segmento, observacoes) VALUES
('PETR4', 'ACAO', 'Petróleo e Gás', 'Ações preferenciais da Petrobras, estatal com bons dividendos.'),
('VALE3', 'ACAO', 'Mineração', 'Principal mineradora brasileira, alta exposição ao preço do minério de ferro.'),
('ITUB4', 'ACAO', 'Bancário', 'Banco Itaú Unibanco, líder no setor financeiro.'),
('BBDC4', 'ACAO', 'Bancário', 'Banco Bradesco, tradicional e com presença nacional.'),
('MGLU3', 'ACAO', 'Varejo', 'Magazine Luiza, empresa de e-commerce e varejo físico.'),
('WEGE3', 'ACAO', 'Indústria', 'Weg S.A., multinacional do setor elétrico e automação.'),
('BBAS3', 'ACAO', 'Bancário', 'Banco do Brasil, instituição financeira controlada pela União.'),
('ABEV3', 'ACAO', 'Consumo', 'Ambev, líder no mercado de bebidas e cervejas na América Latina.'),
('LREN3', 'ACAO', 'Varejo', 'Lojas Renner, uma das maiores redes de moda do Brasil.'),
('EMBR3', 'ACAO', 'Indústria', 'Embraer, fabricante de aeronaves comerciais e executivas.');


UPDATE Ativo SET valor_cota = 39.20  WHERE ticker = 'PETR4';
UPDATE Ativo SET valor_cota = 62.10  WHERE ticker = 'VALE3';
UPDATE Ativo SET valor_cota = 33.80  WHERE ticker = 'ITUB4';
UPDATE Ativo SET valor_cota = 14.50  WHERE ticker = 'BBDC4';
UPDATE Ativo SET valor_cota = 2.80   WHERE ticker = 'MGLU3';
UPDATE Ativo SET valor_cota = 38.90  WHERE ticker = 'WEGE3';
UPDATE Ativo SET valor_cota = 27.40  WHERE ticker = 'BBAS3';
UPDATE Ativo SET valor_cota = 13.90  WHERE ticker = 'ABEV3';
UPDATE Ativo SET valor_cota = 19.50  WHERE ticker = 'LREN3';
UPDATE Ativo SET valor_cota = 28.20  WHERE ticker = 'EMBR3';


-- Criação da tabela dos usuários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    perfil ENUM('INVESTIDOR', 'ADMINISTRADOR') NOT NULL
);


--  Usuários base 
INSERT INTO usuarios (nome, email, senha, cpf, perfil) VALUES
('Gustinho Almeida', 'gustinho.almeida@email.com', '12345', '111.111.111-11', 'INVESTIDOR'),
('Gustinho Barbosa', 'gustinho.barbosa@email.com', '12345', '222.222.222-22', 'INVESTIDOR'),
('Gustinho Carvalho', 'gustinho.carvalho@email.com', '12345', '333.333.333-33', 'ADMINISTRADOR'),
('Gustinho Dias', 'gustinho.dias@email.com', '12345', '444.444.444-44', 'INVESTIDOR'),
('Gustinho Esteves', 'gustinho.esteves@email.com', '12345', '555.555.555-55', 'ADMINISTRADOR'),
('Gustinho Ferreira', 'gustinho.ferreira@email.com', '12345', '666.666.666-66', 'INVESTIDOR'),
('Gustinho Gomes', 'gustinho.gomes@email.com', '12345', '777.777.777-77', 'INVESTIDOR'),
('Gustinho Henrique', 'gustinho.henrique@email.com', '12345', '888.888.888-88', 'ADMINISTRADOR'),
('Gustinho Inácio', 'gustinho.inacio@email.com', '12345', '999.999.999-99', 'INVESTIDOR'),
('Gustinho Justino', 'gustinho.justino@email.com', '12345', '000.000.000-00', 'ADMINISTRADOR');


-- Criação de carteira 
CREATE TABLE carteira (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_investidor INT NOT NULL,
    saldo DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (id_investidor) REFERENCES usuarios(id)
);




CREATE TABLE carteira_ativos (
    id INT PRIMARY KEY AUTO_INCREMENT,

    id_usuario INT NOT NULL,   -- referência direta ao investidor
    id_ativo INT NOT NULL,     -- referência ao ativo

    quantidade_total INT NOT NULL DEFAULT 0,
    preco_medio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_investido DECIMAL(12,2) NOT NULL DEFAULT 0.00,

    ultimo_valor_cota DECIMAL(10,2),

    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_carteira_ativos_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id),

    CONSTRAINT fk_carteira_ativos_ativo FOREIGN KEY (id_ativo)
        REFERENCES ativo(id),

    UNIQUE (id_usuario, id_ativo)  -- garante 1 linha por usuário+ativo
);




-- Tabela para extrato do investidor 
CREATE TABLE movimentacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_carteira INT NOT NULL,
    tipo ENUM('APORTE', 'RETIRADA') NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_carteira) REFERENCES carteira(id)
);


-- Criar a model?: 

-- Registrar sempre uma operação: 
-- Compra → insere uma linha com tipo COMPRA.
-- Venda → insere uma linha com tipo VENDA.
-- Estorno → insere uma linha com tipo ESTORNO, mesma quantidade e valor da operação original, mas invertendo o efeito.

CREATE TABLE carteira_operacoes (
    id SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_ativo INT NOT NULL,
    tipo_operacao VARCHAR(20) NOT NULL, -- COMPRA, VENDA, ESTORNO
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(18,2) NOT NULL,
    valor_total DECIMAL(18,2) NOT NULL,
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FOREIGN KEY ligando id_usuario à tabela usuarios e id_ativo à tabela ativo para garantir consistência.
ALTER TABLE carteira_operacoes
ADD CONSTRAINT fk_operacoes_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
ADD CONSTRAINT fk_operacoes_ativo FOREIGN KEY (id_ativo) REFERENCES ativo(id);

ALTER TABLE carteira_operacoes
ADD COLUMN taxa DECIMAL(10,2) DEFAULT 0.00,
ADD COLUMN observacoes VARCHAR(300);

-- O cálculo da taxa pode ser feito assim:
-- Taxa fixa: adiciona direto ao custo.
-- Taxa percentual: aplica sobre o valor da operação.
-- Pode combinar os dois.

CREATE TABLE corretoras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    taxa_fixa DECIMAL(10,2) DEFAULT 0.00,   -- taxa padrão da corretora
    taxa_percentual DECIMAL(5,2) DEFAULT 0.00, -- taxa percentual opcional
    observacoes VARCHAR(300)
);

-- Padrão: Se o usuário não escolher nenhuma corretora ao registrar uma operação, o sistema automaticamente vincula à corretora SIBOV.
INSERT INTO corretoras (nome, taxa_fixa, taxa_percentual, observacoes)
VALUES ('SIBOV', 0.00, 0.00, 'Corretora padrão do sistema');

-- 

ALTER TABLE carteira_operacoes
ADD COLUMN id_corretora INT DEFAULT 1,
ADD CONSTRAINT fk_operacoes_corretora FOREIGN KEY (id_corretora) REFERENCES corretoras(id);


-- Alterações na estrutura da tabela , sai taxa e entra saldo_após: 
ALTER TABLE carteira_operacoes
DROP COLUMN taxa;

ALTER TABLE carteira_operacoes
ADD COLUMN saldo_após DECIMAL(12,2) DEFAULT 0.00;

-- Criar a Model:



CREATE TABLE proventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_ativo INT NOT NULL,
    tipo ENUM('DIVIDENDO','JCP','RENDIMENTO_FII') NOT NULL,
    valor_por_cota DECIMAL(10,2) NOT NULL,
    
    data_com DATE NOT NULL,        -- último dia com direito
    data_ex DATE NOT NULL,         -- primeiro dia sem direito
    data_pagamento DATE NOT NULL,  -- data do crédito
    
    status ENUM('AGENDADO','PAGO') DEFAULT 'AGENDADO',
    observacoes VARCHAR(300),
    
    FOREIGN KEY (id_ativo) REFERENCES ativo(id)
);


ALTER TABLE carteira_operacoes
ADD COLUMN valor_operacao DECIMAL(18,2) NOT NULL;

ALTER TABLE carteira_operacoes
MODIFY COLUMN tipo_operacao 
    ENUM('DEPOSITO','SAQUE','COMPRA','VENDA','TAXA','PROVENTO','ESTORNO') NOT NULL;

-- Permitir que as culunas id_ativo e id_corretora aceitem valores nulos, pois em saques e depositos elas não existirão. 
ALTER TABLE carteira_operacoes 
MODIFY COLUMN id_ativo INT NULL,
MODIFY COLUMN id_corretora INT NULL;

ALTER TABLE carteira_operacoes 
MODIFY COLUMN id_ativo INT NULL;

DESCRIBE carteira_operacoes;

-- saldo_após estava com acento, feito a corrreção:
ALTER TABLE carteira_operacoes 
CHANGE COLUMN `saldo_após` `saldo_apos` DECIMAL(15,2) NOT NULL;

ALTER TABLE carteira_operacoes MODIFY COLUMN quantidade DECIMAL(18,2) NULL;

ALTER TABLE carteira_operacoes MODIFY COLUMN valor_unitario DECIMAL(18,2) NULL;






