package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.config.TokenService;
import com.br.vaquejadadigital.dtos.request.LoginRequest;
import com.br.vaquejadadigital.dtos.request.RegisterRequest;
import com.br.vaquejadadigital.dtos.response.LoginResponse;
import com.br.vaquejadadigital.dtos.response.UsuarioResponse;
import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.exception.UsernameOrPasswordInvalid;
import com.br.vaquejadadigital.mapper.UsuarioMapper;
import com.br.vaquejadadigital.service.UsuarioService;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegisterRequest request) {
        Usuario savedUsuario = usuarioService.save(usuarioMapper.toUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toResponse(savedUsuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken userAndPass =  new UsernamePasswordAuthenticationToken(request.email(), request.senha());

            Authentication authentication = authenticationManager.authenticate(userAndPass);

            Usuario usuario = (Usuario) authentication.getPrincipal();

            String generatedToken = tokenService.gerenatedToken(usuario);
            return ResponseEntity.ok(new LoginResponse(generatedToken));
        } catch (BadCredentialsException e) {
            throw new UsernameOrPasswordInvalid("Usuário ou senha inválidos.");
        }
    }

}
