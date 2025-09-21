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

    public String gerenatedToken(Usuarios user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userID", user.getId())
                .withExpiresAt(Instant.now().plusSeconds(86400))
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

}
