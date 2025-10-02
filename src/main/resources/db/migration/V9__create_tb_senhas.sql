CREATE TABLE tb_senhas
(
    id                 BIGSERIAL PRIMARY KEY,
    evento_id          BIGINT  NOT NULL,
    dupla_id           BIGINT,
    categoria_id       BIGINT  NOT NULL,
    numero_senha       INTEGER NOT NULL,
    dia_corrida        VARCHAR(20),
    valor_pago         DECIMAL(10, 2),
    pagou_com_desconto BOOLEAN     DEFAULT false,
    forma_pagamento    VARCHAR(50),
    status_pagamento   VARCHAR(50) DEFAULT 'PENDENTE',
    bloqueada          BOOLEAN     DEFAULT false,
    criado_em          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    atualizado_em      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_senha_evento
        FOREIGN KEY (evento_id) REFERENCES tb_eventos (id) ON DELETE CASCADE,

    CONSTRAINT fk_senha_dupla
        FOREIGN KEY (dupla_id) REFERENCES tb_duplas (id) ON DELETE SET NULL,

    CONSTRAINT fk_senha_categoria
        FOREIGN KEY (categoria_id) REFERENCES tb_categorias (id),

    CONSTRAINT uq_senha_numero
        UNIQUE (evento_id, categoria_id, numero_senha)
);

CREATE INDEX idx_senha_evento ON tb_senhas (evento_id);
CREATE INDEX idx_senha_categoria ON tb_senhas (categoria_id);
CREATE INDEX idx_senha_dupla ON tb_senhas (dupla_id);
CREATE INDEX idx_senha_status ON tb_senhas (status_pagamento);
CREATE INDEX idx_senha_dia ON tb_senhas (dia_corrida);