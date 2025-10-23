package com.br.vaquejadadigital.entities;

import com.br.vaquejadadigital.entities.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoNotificacao tipo;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(nullable = false)
    private Boolean lida = false;

    @Column(name = "evento_id")
    private Long eventoId;

    @Column(name = "rodizio_id")
    private Long rodizioId;

    @Column(name = "ordem_corrida_id")
    private Long ordemCorridaId;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;
}
