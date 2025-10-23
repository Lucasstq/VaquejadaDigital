package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.FaseRodizio;
import com.br.vaquejadadigital.entities.enums.StatusRodizio;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rodizio", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"evento_id", "numero_rodizio"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rodizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "numero_rodizio", nullable = false)
    private Integer numeroRodizio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FaseRodizio fase;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusRodizio status = StatusRodizio.AGUARDANDO;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "rodizio", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("posicao ASC")
    @Builder.Default
    private List<OrdemCorrida> ordemCorridas = new ArrayList<>();
}