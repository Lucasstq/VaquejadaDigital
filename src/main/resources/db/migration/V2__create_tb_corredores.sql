CREATE TABLE tb_corredores
(
    id            BIGSERIAL PRIMARY KEY,
    nome_completo VARCHAR(255),
    apelido       VARCHAR(100),
    cpf           VARCHAR(14) UNIQUE,
    telefone      VARCHAR(20),
    cidade        VARCHAR(100),
    foto_perfil   VARCHAR(500),
    data_cadastro DATE             DEFAULT CURRENT_DATE,
    usuario_id    BIGINT  NOT NULL UNIQUE,
    ativo         BOOLEAN NOT NULL DEFAULT true,
    criado_em     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE tb_corredores
    ADD CONSTRAINT fk_corredor_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id) ON DELETE CASCADE;
