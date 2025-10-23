package com.br.vaquejadadigital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_venda", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"venda_id", "senha_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "senha_id", nullable = false)
    private Senha senha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}
