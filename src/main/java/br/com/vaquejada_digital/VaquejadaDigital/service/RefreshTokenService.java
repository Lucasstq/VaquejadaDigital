package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.config.TokenService;
import br.com.vaquejada_digital.VaquejadaDigital.entity.RefreshToken;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    public RefreshToken createRefreshToken(Usuarios usuario, String ip) {

        String tokenJwt = tokenService.gerenatedToken(usuario);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(tokenJwt)
                .usuario(usuario)
                .expiraEm(LocalDateTime.now().plusSeconds(tokenService.getRefreshTokenExpiration()))
                .criadoEm(LocalDateTime.now())
                .revogado(false)
                .ipAddress(ip)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void revogarToken(RefreshToken refreshToken) {
        refreshToken.setRevogado(true);
        refreshTokenRepository.save(refreshToken);
    }

    public void revogarTodosTokensDoUsuario(Usuarios usuario) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUsuarioAndRevogadoFalse(usuario);
        tokens.forEach(token -> token.setRevogado(true));
        refreshTokenRepository.saveAll(tokens);
    }

    public boolean isTokenValid(RefreshToken refreshToken) {
        return !refreshToken.getRevogado() && !refreshToken.isExpired();
    }
}
