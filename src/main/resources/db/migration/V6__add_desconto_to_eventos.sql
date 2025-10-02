ALTER TABLE tb_eventos
    ADD COLUMN preco_com_desconto DECIMAL(10, 2),
ADD COLUMN percentual_desconto INTEGER,
ADD COLUMN valor_abvaq DECIMAL(10,2) DEFAULT 10.00;

UPDATE tb_eventos
SET preco_com_desconto  = preco_base_senha * 0.90,
    percentual_desconto = 10,
    valor_abvaq         = 10.00
WHERE preco_base_senha IS NOT NULL
  AND preco_com_desconto IS NULL;