package br.com.vaquejada_digital.VaquejadaDigital.entity;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.ResultadoCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusChamada;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "tb_ordem_corridas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemCorrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rodizio_id", nullable = false)
    private Rodizio rodizio;

    @ManyToOne
    @JoinColumn(name = "senha_id", nullable = false)
    private Senha senha;

    @Column(name = "posicao", nullable = false)
    private Integer posicao;

    @Column(name = "status_chamada")
    @Enumerated(EnumType.STRING)
    private StatusChamada statusChamada;

    @Column(name = "resultado_corrida")
    @Enumerated(EnumType.STRING)
    private ResultadoCorrida resultado;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "horario_chamada")
    private LocalDateTime horarioChamada;

    @Column(name = "horario_corrida")
    private LocalDateTime horarioCorrida;

    @ManyToOne
    @JoinColumn(name = "juiz_id")
    private Usuarios juiz;

    @CreatedDate
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

}
