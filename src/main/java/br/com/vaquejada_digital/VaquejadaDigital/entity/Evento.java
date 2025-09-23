package br.com.vaquejada_digital.VaquejadaDigital.entity;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "tb_eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    private String local;

    private String descricao;

    @Column(name = "preco_base_senha", precision = 10, scale = 2)
    private BigDecimal precoBaseSenha;

    @Column(name = "senhas_vendidas")
    @Builder.Default
    private Integer senhasVendidas = 0;

    @Column(name = "quantidade_total_senhas")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidadeTotalSenhas;

    @Column(name = "url_midia")
    private List<String> imagensVideos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ATIVO;

    @CreatedDate
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;


}
