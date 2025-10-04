package br.com.vaquejada_digital.VaquejadaDigital.entity;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusRodizio;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.TipoRodizio;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Table(name = "tb_rodizios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rodizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "numero_rodizio")
    private Integer numeroRodizio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rodizio")
    private TipoRodizio tipoRodizio;

    @Column(name = "quantidade_duplas")
    private Integer quantidadeDuplas;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusRodizio status;

    @OneToMany(mappedBy = "rodizio", cascade = CascadeType.ALL)
    private List<OrdemCorrida> ordens;

    @CreatedDate
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

}
