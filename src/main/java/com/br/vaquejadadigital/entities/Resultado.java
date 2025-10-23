package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.TipoResultado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ordem_corrida_id", unique = true, nullable = false)
    private OrdemCorrida ordemCorrida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_resultado", nullable = false, length = 20)
    private TipoResultado tipoResultado;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "juiz_id", nullable = false)
    private Usuario juiz;

    @Column(name = "tempo_corrida", precision = 5, scale = 2)
    private BigDecimal tempoCorrida;

    @CreationTimestamp
    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

}