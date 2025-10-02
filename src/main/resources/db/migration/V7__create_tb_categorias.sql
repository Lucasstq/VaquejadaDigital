CREATE TABLE tb_categorias
(
    id            BIGSERIAL PRIMARY KEY,
    descricao     VARCHAR(100) NOT NULL UNIQUE,
    informacoes   TEXT,
    criado_em     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
