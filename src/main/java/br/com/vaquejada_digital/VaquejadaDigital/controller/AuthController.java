package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.config.TokenService;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.LoginResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuariosResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.LoginRequest;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.UsuariosRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.RefreshToken;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.exceptions.UsernameOrPasswordInvalid;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.UsuariosMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.RefreshTokenService;
import br.com.vaquejada_digital.VaquejadaDigital.service.UsuariosService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaquejada-digital/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuariosService usuariosService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.access-token.expiration}")
    Long accessTokenExpiration;

    @PostMapping("/registro")
    public ResponseEntity<UsuariosResponse> ciarUsuario(@Valid @RequestBody UsuariosRequest request) {
        Usuarios created = usuariosService.criarUsuario(UsuariosMapper.toUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuariosMapper.toUserResponse(created));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        try {
            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            Usuarios user = (Usuarios) authentication.getPrincipal();

            String token = tokenService.gerenatedToken(user);

            String ip = httpServletRequest.getRemoteAddr();
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, ip);

            return ResponseEntity.ok(new LoginResponse(token, refreshToken.getToken(), accessTokenExpiration));
        } catch (BadCredentialsException e) {
            throw new UsernameOrPasswordInvalid("usuario ou senha incorreto");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Refresh-token") String refreshTokenJwt) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenJwt)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (!refreshTokenService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh Token expirado ou revogado.");
        }

        Usuarios usuario = refreshToken.getUsuario();
        String novoAccessToken = tokenService.gerenatedToken(usuario);


        return ResponseEntity.ok(new LoginResponse(
                novoAccessToken,
                refreshTokenJwt,
                accessTokenExpiration
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Refresh-Token") String refreshTokenJwt) {

        refreshTokenService.findByToken(refreshTokenJwt)
                .ifPresent(refreshTokenService::revogarToken);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(Authentication authentication) {
        Usuarios usuario = (Usuarios) authentication.getPrincipal();
        refreshTokenService.revogarTodosTokensDoUsuario(usuario);
        return ResponseEntity.noContent().build();
    }

}
