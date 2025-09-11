ALTER TABLE evento DROP COLUMN imagens_videos;

CREATE TABLE evento_midias
(
    evento_id BIGINT NOT NULL,
    midia     TEXT   NOT NULL,
    CONSTRAINT fk_evento FOREIGN KEY (evento_id) REFERENCES evento (id)
);
