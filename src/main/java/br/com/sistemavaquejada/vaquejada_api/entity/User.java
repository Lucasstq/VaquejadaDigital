package br.com.sistemavaquejada.vaquejada_api.entity;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Perfil;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "nome")
    String nome;

    @Column(unique = true, nullable = false, name = "email")
    String email;

    @Column(nullable = false, name = "senha")
    String senha;

    @Column(unique = true, nullable = false, name = "telefone")
    String telefone;

    @Enumerated(EnumType.STRING)
    Perfil perfil;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    public boolean hasAuthorityLevel(Perfil requiredPerfil) {
        Map<Perfil, Integer> levels = Map.of(
                Perfil.CORREDOR, 1,
                Perfil.LOCUTOR, 2,
                Perfil.JUIZ, 3,
                Perfil.ADMIN, 4
        );

        return levels.get(this.perfil) >= levels.get(requiredPerfil);
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
