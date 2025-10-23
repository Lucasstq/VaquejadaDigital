package com.br.vaquejadadigital.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "corredor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Corredor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(length = 50)
    private String apelido;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

}
