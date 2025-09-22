package br.com.vaquejada_digital.VaquejadaDigital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "tb_corredores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Corredor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    private String apelido;

    @Column(unique = true, length = 14)
    private String cpf;

    private String telefone;

    private String cidade;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @Builder.Default
    private Boolean ativo = true;

    @CreatedDate
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

}
