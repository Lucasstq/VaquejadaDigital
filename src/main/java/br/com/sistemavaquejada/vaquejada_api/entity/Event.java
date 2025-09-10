package br.com.sistemavaquejada.vaquejada_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    @Column(name = "dataInicio")
    private LocalDate dataInicio;

    @UpdateTimestamp
    @Column(name = "dataFim")
    private LocalDate dataFim;

    @Column(length = 100, nullable = false)
    private String local;

    @Column(length = 300, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double precoBaseSenha;

    private Integer quantidadeTotalDeSenha;

    private List<String> imagensVideos;

    private Status status;
}
