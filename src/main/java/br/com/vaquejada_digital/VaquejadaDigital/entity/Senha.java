package br.com.vaquejada_digital.VaquejadaDigital.entity;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.FormaPagamento;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_senhas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Senha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "dupla_id")
    private Dupla dupla;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "numero_senha", nullable = false)
    private Integer numeroSenha;

    @Column(name = "dia_corrida")
    private String diaCorrida;

    @Column(name = "valor_pago", precision = 10, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "pagou_com_desconto")
    @Builder.Default
    private Boolean pagouComDesconto = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento")
    @Builder.Default
    private StatusPagamento statusPagamento = StatusPagamento.PENDENTE;

    @Builder.Default
    @Column(nullable = false)
    private Boolean bloqueada = false;

    @CreatedDate
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public boolean isDisponivel() {
        return dupla == null &&
                !bloqueada &&
                statusPagamento != StatusPagamento.PAGO;
    }

    public void venderComDesconto(Dupla dupla, String diaCorrida, FormaPagamento forma) {
        if (!isDisponivel()) {
            throw new IllegalStateException("Senha não disponível");
        }

        this.dupla = dupla;
        this.diaCorrida = diaCorrida;

        this.valorPago = evento.getPrecoComDesconto().add(evento.getValorAbvaq());
        this.pagouComDesconto = true;
        this.formaPagamento = forma;
        this.statusPagamento = StatusPagamento.PAGO;

        evento.incrementarSenhasVendidas();
    }


    public void venderSemDesconto(Dupla dupla, String diaCorrida, FormaPagamento forma) {
        if (!isDisponivel()) {
            throw new IllegalStateException("Senha não disponível");
        }

        this.dupla = dupla;
        this.diaCorrida = diaCorrida;


        this.valorPago = evento.getPrecoBaseSenha().add(evento.getValorAbvaq());
        this.pagouComDesconto = false;
        this.formaPagamento = forma;
        this.statusPagamento = StatusPagamento.PAGO;

        evento.incrementarSenhasVendidas();
    }
}
