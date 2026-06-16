CREATE DATABASE IF NOT EXISTS ticket;
USE ticket;

CREATE TABLE IF NOT EXISTS funcao (
    id_funcao INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL
);

INSERT INTO funcao (nome, ativo)
VALUES ('Admin', 1);

CREATE TABLE IF NOT EXISTS funcao_permissao (
	id_funcao INT NOT NULL,
	permissao VARCHAR(50) NOT NULL,

	FOREIGN KEY (id_funcao)
	REFERENCES funcao(id_funcao)
	 	ON DELETE CASCADE
);

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'ABRIR_TICKET'
FROM funcao
WHERE nome = 'Admin'
LIMIT 1;

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'RESPONDER_TICKET'
FROM funcao
WHERE nome = 'Admin'
LIMIT 1;

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'FECHAR_TICKET'
FROM funcao
WHERE nome = 'Admin'
LIMIT 1;

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'CADASTRAR_USUARIO'
FROM funcao
WHERE nome = 'Admin'
LIMIT 1;

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'CADASTRAR_FUNCAO'
FROM funcao
WHERE nome = 'Admin'
LIMIT 1;

CREATE TABLE IF NOT EXISTS usuario (
	id INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
	email VARCHAR(150) NOT NULL UNIQUE,
	senha VARCHAR(255) NOT NULL,
	data_cadastro DATE NOT NULL,
	setor VARCHAR(50) NOT NULL,
	id_funcao INT,

	FOREIGN KEY (id_funcao)
	REFERENCES funcao(id_funcao)
);

INSERT INTO usuario (nome, email, senha, data_cadastro, setor, id_funcao)
SELECT 'Administrador', 'admin@ticket.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', SYSDATE(), 'ADMINISTRACAO', id_funcao
FROM funcao
WHERE nome = 'Admin'
LIMIT 1;

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT NOT NULL,
    setor_destino VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    prioridade VARCHAR(50) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    data_abertura DATETIME NOT NULL,
    data_fechamento DATETIME,
    id_criado_por INT NOT NULL,
    id_respondido_por INT,

    FOREIGN KEY (id_criado_por)
    REFERENCES usuario(id),
    FOREIGN KEY (id_respondido_por)
    REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS mensagem (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket  INT NOT NULL,
    id_autor   INT NOT NULL,
    texto      TEXT NOT NULL,
    data_envio DATETIME NOT NULL,

    FOREIGN KEY (id_ticket)
        REFERENCES ticket(id)
        ON DELETE CASCADE,
    FOREIGN KEY (id_autor)
        REFERENCES usuario(id)
);

CREATE TABLE arquivo_ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_arquivo VARCHAR(255) NOT NULL,
    tipo_arquivo VARCHAR(100),
    arquivo MEDIUMBLOB NOT NULL,
    data_envio DATETIME NOT NULL,
    id_ticket INT NOT NULL,
    id_enviado_por INT NOT NULL,

    FOREIGN KEY (id_ticket) REFERENCES ticket(id),
    FOREIGN KEY (id_enviado_por) REFERENCES usuario(id)
);
