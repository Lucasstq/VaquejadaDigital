package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.RefreshToken;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUsuarioAndRevogadoFalse(Usuarios usuario);

    void deleteByUsuairo(Usuarios usuario);

}
