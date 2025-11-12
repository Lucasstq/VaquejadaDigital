package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_corredor", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"corredor_id", "evento_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoCorredor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corredor_id", nullable = false)
    private Corredor corredor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCategoria categoria;

    // Estatísticas do evento
    @Column(name = "total_corridas", nullable = false)
    private Integer totalCorridas = 0;

    @Column(name = "vale_boi", nullable = false)
    private Integer valeBoi = 0;

    @Column(name = "zeros", nullable = false)
    private Integer zeros = 0;

    @Column(name = "retornos", nullable = false)
    private Integer retornos = 0;

    @Column(name = "faltas", nullable = false)
    private Integer faltas = 0;

    @Column(name = "desclassificacoes", nullable = false)
    private Integer desclassificacoes = 0;

    // Pontuação e posição
    @Column(name = "pontuacao_total", precision = 10, scale = 2)
    private BigDecimal pontuacaoTotal = BigDecimal.ZERO;

    @Column(name = "posicao_final")
    private Integer posicaoFinal;

    @Column(name = "colocacao_geral")
    private String colocacaoGeral;

    // Informações adicionais
    @Column(name = "melhor_tempo")
    private String melhorTempo;

    @Column(name = "taxa_sucesso", precision = 5, scale = 2)
    private BigDecimal taxaSucesso = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    //Calcula a taxa de sucesso (percentual de "Vale o Boi")
    public void calcularTaxaSucesso() {
        if (totalCorridas > 0) {
            this.taxaSucesso = BigDecimal.valueOf(valeBoi)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalCorridas), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            this.taxaSucesso = BigDecimal.ZERO;
        }
    }

    //Incrementa estatísticas com base no resultado
    public void incrementarEstatistica(String tipoResultado) {
        this.totalCorridas++;

        switch (tipoResultado.toUpperCase()) {
            case "VALEU_BOI":
            case "VALEU_O_BOI":
            case "VALEU":
                this.valeBoi++;
                break;
            case "ZERO":
                this.zeros++;
                break;
            case "RETORNO":
                this.retornos++;
                break;
            case "FALTA":
                this.faltas++;
                break;
            case "DESCLASSIFICADO":
            case "DESCLASSIFICACAO":
                this.desclassificacoes++;
                break;
        }

        calcularTaxaSucesso();
    }
}
