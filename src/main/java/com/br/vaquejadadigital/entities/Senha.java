package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.StatusSenha;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "senha", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"evento_id", "numero_senha", "categoria_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Senha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "dupla_id")
    private Dupla dupla;

    @Column(name = "numero_senha", nullable = false)
    private Integer numeroSenha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSenha status = StatusSenha.DISPONIVEL;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

}