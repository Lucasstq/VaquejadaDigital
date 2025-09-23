CREATE TABLE tb_eventos
(
    id                      BIGSERIAL PRIMARY KEY,
    nome                    VARCHAR(255) NOT NULL,
    data_inicio             DATE,
    data_fim                DATE,
    local                   VARCHAR(255),
    descricao               TEXT,
    preco_base_senha        DECIMAL(10, 2),
    quantidade_total_senhas INTEGER,
    status                  VARCHAR(50)  NOT NULL DEFAULT 'ATIVO',
    criado_em               TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    atualizado_em           TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_evento_midias
(
    evento_id BIGINT NOT NULL,
    url_midia VARCHAR(500),
    CONSTRAINT fk_evento_midias_evento
        FOREIGN KEY (evento_id) REFERENCES tb_eventos (id) ON DELETE CASCADE
);