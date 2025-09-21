CREATE TABLE tb_usuarios
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    senha         VARCHAR(255) NOT NULL,
    nome          VARCHAR(255),
    telefone      VARCHAR(20),
    tipo_perfil   VARCHAR(50)  NOT NULL,
    ativo         BOOLEAN      NOT NULL DEFAULT true,
    criado_em     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);