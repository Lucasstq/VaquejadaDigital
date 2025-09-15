package br.com.sistemavaquejada.vaquejada_api.service;

import br.com.sistemavaquejada.vaquejada_api.entity.User;
import br.com.sistemavaquejada.vaquejada_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User save(User user) {
        String senha = user.getSenha();
        user.setSenha(encoder.encode(senha));
        return repository.save(user);
    }


}
