package br.com.vaquejada_digital.VaquejadaDigital.config;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    String secretKey;

    @Value("${jwt.acess-token.expiration:900}")
    Long acesTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800}")
    Long refreshTokenExpiration;

    public String gerenatedToken(Usuarios user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userID", user.getId())
                .withClaim("tipo", "ACESS")
                .withExpiresAt(Instant.now().plusSeconds(acesTokenExpiration))
                .withIssuedAt(Instant.now())
                .withIssuer("Vaquejada-Digital")
                .sign(algorithm);

    }

    public String gerenatedRefreshToken(Usuarios user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userID", user.getId())
                .withClaim("tipo", "REFRESH")
                .withExpiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
                .withIssuedAt(Instant.now())
                .withIssuer("Vaquejada-Digital")
                .sign(algorithm);
    }

    public Optional<JWTUserData> verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData.builder()
                    .id(jwt.getClaim("userID").asLong())
                    .email(jwt.getSubject())
                    .build());

        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }

    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

}
