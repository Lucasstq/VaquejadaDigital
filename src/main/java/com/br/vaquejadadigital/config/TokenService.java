package com.br.vaquejadadigital.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.vaquejadadigital.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String gerenatedToken(Usuario user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId())
                .withClaim("nome", user.getNome())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .withIssuer("Vaquejada-Digital-token")
                .sign(algorithm);

    }


    public Optional<JWTUserData> verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData
                    .builder()
                    .id(decodedJWT.getClaim("userId").asLong())
                    .email(decodedJWT.getSubject())
                    .build());
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}

