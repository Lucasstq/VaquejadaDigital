package br.com.sistemavaquejada.vaquejada_api.mapper;

import br.com.sistemavaquejada.vaquejada_api.controller.request.UserRequest;
import br.com.sistemavaquejada.vaquejada_api.controller.response.UserResponse;
import br.com.sistemavaquejada.vaquejada_api.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toUser(UserRequest userRequest) {
        return User
                .builder()
                .nome(userRequest.nome())
                .email(userRequest.email())
                .telefone(userRequest.telefone())
                .senha(userRequest.senha())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nome(user.getNome())
                .telefone(user.getTelefone())
                .perfil(user.getPerfil())
                .build();
    }

}
