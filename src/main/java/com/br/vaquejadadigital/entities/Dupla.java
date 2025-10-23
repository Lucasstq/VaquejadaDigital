package com.br.vaquejadadigital.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
//Define restrições de unicidade (chaves únicas) sobre uma ou mais colunas.
@Table(name = "dupla", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"puxador_id", "esteireiro_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dupla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "puxador_id", nullable = false)
    private Corredor puxador;

    @ManyToOne
    @JoinColumn(name = "esteireiro_id", nullable = false)
    private Corredor esteireiro;

    @Column(name = "nome_dupla", length = 100)
    private String nomeDupla;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    @PreUpdate
    private void validarDupla() {
        if (puxador != null && esteireiro != null && puxador.getId().equals(esteireiro.getId())) {
            throw new IllegalArgumentException("Puxador e esteireiro não podem ser a mesma pessoa");
        }
    }
}
