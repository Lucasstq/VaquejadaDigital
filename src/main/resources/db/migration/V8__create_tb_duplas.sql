CREATE TABLE tb_duplas
(
    id            BIGSERIAL PRIMARY KEY,
    puxador_id    BIGINT NOT NULL,
    esteireiro_id BIGINT NOT NULL,
    nome_dupla    VARCHAR(255),
    criado_em     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_dupla_puxador
        FOREIGN KEY (puxador_id) REFERENCES tb_corredores (id) ON DELETE CASCADE,

    CONSTRAINT fk_dupla_esteireiro
        FOREIGN KEY (esteireiro_id) REFERENCES tb_corredores (id) ON DELETE CASCADE,

    CONSTRAINT chk_dupla_diferente CHECK (puxador_id != esteireiro_id
) ,

    CONSTRAINT uq_dupla_par UNIQUE (puxador_id, esteireiro_id)
);

CREATE INDEX idx_dupla_puxador ON tb_duplas (puxador_id);
CREATE INDEX idx_dupla_esteireiro ON tb_duplas (esteireiro_id);