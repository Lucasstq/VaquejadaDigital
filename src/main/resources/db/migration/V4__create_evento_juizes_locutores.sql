CREATE TABLE tb_evento_juizes
(
    evento_id  BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    PRIMARY KEY (evento_id, usuario_id),
    CONSTRAINT fk_evento_juiz_evento FOREIGN KEY (evento_id) REFERENCES tb_eventos (id) ON DELETE CASCADE,
    CONSTRAINT fk_evento_juiz_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id) ON DELETE CASCADE
);

CREATE TABLE tb_evento_locutores
(
    evento_id  BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    PRIMARY KEY (evento_id, usuario_id),
    CONSTRAINT fk_evento_locutor_evento FOREIGN KEY (evento_id) REFERENCES tb_eventos (id) ON DELETE CASCADE,
    CONSTRAINT fk_evento_locutor_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id) ON DELETE CASCADE
);