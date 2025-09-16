package br.com.sistemavaquejada.vaquejada_api.entity;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Entity
@Table(name = "Evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(name = "dataInicio")
    private LocalDate dataInicio;

    @Column(name = "dataFim")
    private LocalDate dataFim;

    @Column(length = 100, nullable = false)
    private String local;

    @Column(length = 300, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double precoBaseSenha;

    private Integer quantidadeTotalDeSenha;

    @ElementCollection
    @CollectionTable(name = "evento_midias",
            joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "midia")
    private List<String> imagensVideos;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ATIVO;
}
