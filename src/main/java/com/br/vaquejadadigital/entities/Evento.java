package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.StatusEvento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 200)
    private String local;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "quantidade_senhas_total", nullable = false)
    private Integer quantidadeSenhasTotal;

    @Column(name = "quantidade_senhas_vendidas")
    private Integer quantidadeSenhasVendidas = 0;

    @Column(name = "valor_senha", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorSenha;

    @Column(name = "limite_duplas_por_rodizio")
    private Integer limiteDuplasPorRodizio = 10;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusEvento status = StatusEvento.CRIADO;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Senha> senhas = new ArrayList<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Rodizio> rodizios = new ArrayList<>();

}
