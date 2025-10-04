CREATE TABLE tb_ordem_corrida
(
    id              BIGSERIAL PRIMARY KEY,
    rodizio_id      BIGINT  NOT NULL,
    senha_id        BIGINT  NOT NULL,
    posicao         INTEGER NOT NULL,
    status_chamada  VARCHAR(50) DEFAULT 'AGUARDANDO',
    resultado       VARCHAR(50),
    observacoes     TEXT,
    horario_chamada TIMESTAMP,
    horario_corrida TIMESTAMP,
    juiz_id         BIGINT,
    criado_em       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ordem_rodizio
        FOREIGN KEY (rodizio_id) REFERENCES tb_rodizios (id) ON DELETE CASCADE,
    CONSTRAINT fk_ordem_senha
        FOREIGN KEY (senha_id) REFERENCES tb_senhas (id),
    CONSTRAINT fk_ordem_juiz
        FOREIGN KEY (juiz_id) REFERENCES tb_usuarios (id),

    CONSTRAINT uq_rodizio_posicao UNIQUE (rodizio_id, posicao)
);

CREATE INDEX idx_ordem_rodizio ON tb_ordem_corrida (rodizio_id);
CREATE INDEX idx_ordem_senha ON tb_ordem_corrida (senha_id);
CREATE INDEX idx_ordem_status ON tb_ordem_corrida (status_chamada);