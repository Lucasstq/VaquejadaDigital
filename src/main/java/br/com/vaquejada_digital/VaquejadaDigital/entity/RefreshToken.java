package br.com.vaquejada_digital.VaquejadaDigital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "tb_refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @Column(name = "expira_em")
    private LocalDateTime expiraEm;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "revogado")
    private Boolean revogado = false;

    @Column(name = "ip_address")
    private String ipAddress;


    public Boolean isExpired() {
        return LocalDateTime.now().isAfter(expiraEm);
    }

}
