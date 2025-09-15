package br.com.sistemavaquejada.vaquejada_api.controller;

import br.com.sistemavaquejada.vaquejada_api.config.TokenService;
import br.com.sistemavaquejada.vaquejada_api.controller.request.LoginRequest;
import br.com.sistemavaquejada.vaquejada_api.controller.request.UserRequest;
import br.com.sistemavaquejada.vaquejada_api.controller.response.LoginResponse;
import br.com.sistemavaquejada.vaquejada_api.controller.response.UserResponse;
import br.com.sistemavaquejada.vaquejada_api.entity.User;
import br.com.sistemavaquejada.vaquejada_api.exception.UsernameOrPasswordInvalid;
import br.com.sistemavaquejada.vaquejada_api.mapper.UserMapper;
import br.com.sistemavaquejada.vaquejada_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaquejada/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest user) {
        User save = userService.save(UserMapper.toUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserResponse(save));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();

            String token = tokenService.gerenatedToken(user);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException e) {
            throw new UsernameOrPasswordInvalid("Email ou Senha incorreto.");
        }
    }


}
