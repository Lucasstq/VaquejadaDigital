CREATE TABLE tb_rodizios (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    numero_rodizio INTEGER NOT NULL,
    tipo_rodizio VARCHAR(50) DEFAULT 'NORMAL',
    quantidade_duplas INTEGER,
    status VARCHAR(50) DEFAULT 'AGUARDANDO',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rodizio_evento
        FOREIGN KEY (evento_id) REFERENCES tb_eventos(id) ON DELETE CASCADE,
    CONSTRAINT fk_rodizio_categoria
        FOREIGN KEY (categoria_id) REFERENCES tb_categorias(id)
);

CREATE INDEX idx_rodizio_evento ON tb_rodizios(evento_id);
CREATE INDEX idx_rodizio_status ON tb_rodizios(status);