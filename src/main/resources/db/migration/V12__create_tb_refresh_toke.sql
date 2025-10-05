CREATE TABLE tb_refresh_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(500) NOT NULL UNIQUE,
    usuario_id  BIGINT       NOT NULL,
    expira_em   TIMESTAMP    NOT NULL,
    criado_em   TIMESTAMP    NOT NULL,
    revogado    BOOLEAN DEFAULT false,
    dispositivo VARCHAR(255),
    ip_address  VARCHAR(45),

    CONSTRAINT fk_refresh_token_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_usuario ON tb_refresh_tokens (usuario_id);
CREATE INDEX idx_refresh_token_token ON tb_refresh_tokens (token);
CREATE INDEX idx_refresh_token_expira ON tb_refresh_tokens (expira_em);