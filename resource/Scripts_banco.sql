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

-- admin -> login: admin@ticket.com / senha: admin
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

INSERT INTO funcao (nome, ativo) VALUES
('Atendente TI', 1),
('Solicitante', 1),
('Gestor RH', 1);

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'ABRIR_TICKET' FROM funcao WHERE nome = 'Atendente TI';
INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'RESPONDER_TICKET' FROM funcao WHERE nome = 'Atendente TI';
INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'FECHAR_TICKET' FROM funcao WHERE nome = 'Atendente TI';

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'ABRIR_TICKET' FROM funcao WHERE nome = 'Solicitante';

INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'ABRIR_TICKET' FROM funcao WHERE nome = 'Gestor RH';
INSERT INTO funcao_permissao (id_funcao, permissao)
SELECT id_funcao, 'CADASTRAR_USUARIO' FROM funcao WHERE nome = 'Gestor RH';

-- senha de todos os usuarios abaixo: 123 (o valor e o hash SHA-256 de "123")
SET @senha = 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3';
SET @atendente = (SELECT id_funcao FROM funcao WHERE nome = 'Atendente TI');
SET @solicitante = (SELECT id_funcao FROM funcao WHERE nome = 'Solicitante');
SET @gestor = (SELECT id_funcao FROM funcao WHERE nome = 'Gestor RH');

INSERT INTO usuario (nome, email, senha, data_cadastro, setor, id_funcao) VALUES
('Rafael Capeletti', 'rafael@ticket.com', @senha, NOW(), 'TI', @atendente),
('Maikon Junior', 'maikon@ticket.com', @senha, NOW(), 'TI', @atendente),
('Julio Cesar', 'julio@ticket.com', @senha, NOW(), 'TI', @atendente),
('Ana Souza', 'ana@ticket.com', @senha, NOW(), 'FINANCEIRO', @solicitante),
('Bruno Lima', 'bruno@ticket.com', @senha, NOW(), 'COMERCIAL', @solicitante),
('Carla Dias', 'carla@ticket.com', @senha, NOW(), 'RH', @gestor);

SET @rafael = (SELECT id FROM usuario WHERE email = 'rafael@ticket.com');
SET @maikon = (SELECT id FROM usuario WHERE email = 'maikon@ticket.com');
SET @julio = (SELECT id FROM usuario WHERE email = 'julio@ticket.com');
SET @ana = (SELECT id FROM usuario WHERE email = 'ana@ticket.com');
SET @bruno = (SELECT id FROM usuario WHERE email = 'bruno@ticket.com');
SET @carla = (SELECT id FROM usuario WHERE email = 'carla@ticket.com');

INSERT INTO ticket (titulo, descricao, setor_destino, status, prioridade, categoria, data_abertura, data_fechamento, id_criado_por, id_respondido_por) VALUES
('Sistema de vendas fora do ar', 'O sistema de vendas parou e ninguem consegue faturar.', 'TI', 'ABERTO', 'URGENTE', 'INCIDENTE', NOW() - INTERVAL 3 HOUR, NULL, @ana, NULL),
('Instalar pacote Office na maquina nova', 'Recebi um notebook novo e preciso do Office instalado.', 'TI', 'ABERTO', 'ALTA', 'SOLICITACAO', NOW() - INTERVAL 1 DAY, NULL, @bruno, NULL),
('Como configurar a VPN no notebook', 'Vou trabalhar de casa e preciso acessar a rede interna.', 'TI', 'ABERTO', 'MEDIA', 'DUVIDA', NOW() - INTERVAL 2 DAY, NULL, @carla, NULL),
('Trocar mouse com defeito', 'O mouse esta falhando o clique, queria a troca.', 'TI', 'ABERTO', 'BAIXA', 'MELHORIA', NOW() - INTERVAL 4 DAY, NULL, @ana, NULL),
('Email corporativo nao envia', 'Desde ontem o email da erro ao enviar mensagens.', 'TI', 'EM_ANDAMENTO', 'ALTA', 'INCIDENTE', NOW() - INTERVAL 1 DAY, NULL, @bruno, @rafael),
('Impressora do RH travada', 'A impressora do setor parou no meio de um documento.', 'TI', 'EM_ANDAMENTO', 'URGENTE', 'INCIDENTE', NOW() - INTERVAL 6 HOUR, NULL, @carla, @maikon),
('Resetar senha do sistema', 'Esqueci minha senha e nao consigo entrar.', 'TI', 'FECHADO', 'MEDIA', 'SOLICITACAO', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 4 DAY, @carla, @julio),
('Duvida sobre segunda via de nota', 'Preciso saber como emitir a segunda via da nota fiscal.', 'FINANCEIRO', 'ABERTO', 'BAIXA', 'DUVIDA', NOW() - INTERVAL 2 DAY, NULL, @bruno, NULL);

SET @t_email = (SELECT id FROM ticket WHERE titulo = 'Email corporativo nao envia');
SET @t_impressora = (SELECT id FROM ticket WHERE titulo = 'Impressora do RH travada');
SET @t_senha = (SELECT id FROM ticket WHERE titulo = 'Resetar senha do sistema');

INSERT INTO mensagem (id_ticket, id_autor, texto, data_envio) VALUES
(@t_email, @bruno, 'Quando tento enviar aparece erro de conexao com o servidor.', NOW() - INTERVAL 23 HOUR),
(@t_email, @rafael, 'Bom dia Bruno, estou verificando o servidor de email, ja retorno.', NOW() - INTERVAL 22 HOUR),
(@t_impressora, @carla, 'A impressora parou e esta com a luz vermelha piscando.', NOW() - INTERVAL 5 HOUR),
(@t_impressora, @maikon, 'Carla, vou ate o setor verificar o equipamento agora.', NOW() - INTERVAL 4 HOUR),
(@t_senha, @carla, 'Nao consigo logar, podem resetar minha senha?', NOW() - INTERVAL 5 DAY),
(@t_senha, @julio, 'Senha resetada, tente entrar e troque depois.', NOW() - INTERVAL 4 DAY),
(@t_senha, @carla, 'Funcionou, obrigada!', NOW() - INTERVAL 4 DAY);
