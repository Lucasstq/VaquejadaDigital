package br.com.sistemavaquejada.vaquejada_api.service;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Perfil;
import br.com.sistemavaquejada.vaquejada_api.entity.User;
import br.com.sistemavaquejada.vaquejada_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User save(User user) {

        if (user.getPerfil() != null) {
            user.setPerfil(Perfil.CORREDOR);
        }

        String senha = user.getSenha();
        user.setSenha(encoder.encode(senha));
        return repository.save(user);
    }

    public User updateUser(Long id, Perfil perfil) {
        User uptUser = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

        uptUser.setPerfil(perfil);
        return repository.save(uptUser);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
