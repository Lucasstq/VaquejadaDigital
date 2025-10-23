package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.StatusCorrida;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordem_corrida", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rodizio_id", "posicao"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemCorrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rodizio_id", nullable = false)
    private Rodizio rodizio;

    @ManyToOne
    @JoinColumn(name = "dupla_id", nullable = false)
    private Dupla dupla;

    @Column(nullable = false)
    private Integer posicao;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusCorrida status = StatusCorrida.PRONTO;

    @Column(name = "data_chamada")
    private LocalDateTime dataChamada;

    @Column(name = "data_corrida")
    private LocalDateTime dataCorrida;

    @OneToOne(mappedBy = "ordemCorrida", cascade = CascadeType.ALL)
    private Resultado resultado;

}
