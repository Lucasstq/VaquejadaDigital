package br.com.vaquejada_digital.VaquejadaDigital.entity;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Perfil;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
@Entity
@Table(name = "tb_usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String nome;

    private String telefone;

    @Column(name = "tipo_perfil")
    @Enumerated(EnumType.STRING)
    private Perfil tipoPerfil;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @CreatedDate
    private LocalDateTime criadoEm;

    @LastModifiedDate
    private LocalDateTime atualizadoEm;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Corredor corredor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoPerfil.name()));
    }

    public boolean hasAuthorityLevel(Perfil requiredPerfil) {
        Map<Perfil, Integer> levels = Map.of(
                Perfil.CORREDOR, 1,
                Perfil.LOCUTOR, 2,
                Perfil.JUIZ, 3,
                Perfil.ADMIN, 4
        );

        return levels.get(this.tipoPerfil) >= levels.get(requiredPerfil);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


