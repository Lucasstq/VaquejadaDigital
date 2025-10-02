package br.com.vaquejada_digital.VaquejadaDigital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "tb_duplas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "nome_dupla")
    private String nomeDupla;

    @CreatedDate
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    @PreUpdate
    private void validar() {
        if (puxador != null && esteireiro != null &&
                puxador.getId().equals(esteireiro.getId())) {
            throw new IllegalArgumentException(
                    "Puxador e Esteireiro devem ser pessoas diferentes"
            );
        }
    }

    public String getNomeDuplaFormatado() {
        if (nomeDupla != null && !nomeDupla.isBlank()) {
            return nomeDupla;
        }

        String nomePuxador = puxador.getApelido() != null ?
                puxador.getApelido() :
                puxador.getNomeCompleto();

        String nomeEsteireiro = esteireiro.getApelido() != null ?
                esteireiro.getApelido() :
                esteireiro.getNomeCompleto();

        return nomePuxador + " & " + nomeEsteireiro;
    }

}
