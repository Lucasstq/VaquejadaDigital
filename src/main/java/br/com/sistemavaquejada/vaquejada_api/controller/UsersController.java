package br.com.sistemavaquejada.vaquejada_api.controller;

import br.com.sistemavaquejada.vaquejada_api.controller.request.UpdatePerfilRequest;
import br.com.sistemavaquejada.vaquejada_api.controller.response.UserResponse;
import br.com.sistemavaquejada.vaquejada_api.entity.User;
import br.com.sistemavaquejada.vaquejada_api.mapper.UserMapper;
import br.com.sistemavaquejada.vaquejada_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaquejada/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @PutMapping("/{id}/perfil")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updatePerfil(@PathVariable Long id, @Valid @RequestBody UpdatePerfilRequest request) {
        User updateUser = userService.updateUser(id, request.perfil());
        return ResponseEntity.ok(UserMapper.toUserResponse(updateUser));

    }


}
