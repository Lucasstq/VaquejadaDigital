package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.FormaPagamento;
import com.br.vaquejadadigital.entities.enums.StatusPagamento;
import com.br.vaquejadadigital.entities.enums.TipoVenda;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Usuario comprador;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private StatusPagamento statusPagamento = StatusPagamento.PENDENTE;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_venda", nullable = false, length = 20)
    private TipoVenda tipoVenda;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "data_venda", nullable = false)
    @CreationTimestamp
    private LocalDateTime dataVenda;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "id_transacao_gateway")
    private String idTransacaoGateway;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemVenda> itens = new ArrayList<>();
}