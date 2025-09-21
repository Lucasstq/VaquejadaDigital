package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.config.TokenService;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.LoginResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuariosResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.LoginRequest;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.UsuariosRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.exceptions.UsernameOrPasswordInvalid;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.UsuariosMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.UsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vaquejada-digital/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuariosService usuariosService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/registro")
    public ResponseEntity<UsuariosResponse> ciarUsuario(@Valid @RequestBody UsuariosRequest request) {
        Usuarios created = usuariosService.criarUsuario(UsuariosMapper.toUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuariosMapper.toUserResponse(created));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            Usuarios user = (Usuarios) authentication.getPrincipal();

            String token = tokenService.gerenatedToken(user);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException e) {
            throw new UsernameOrPasswordInvalid("usuario ou senha incorreto");
        }
    }

}
