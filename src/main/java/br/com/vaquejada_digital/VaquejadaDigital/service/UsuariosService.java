package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Perfil;
import br.com.vaquejada_digital.VaquejadaDigital.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuariosService {

    private final UsuariosRepository repository;
    private final PasswordEncoder encoder;

    public Usuarios criarUsuario(Usuarios usuario) {
        if (usuario.getTipoPerfil() == null) usuario.setTipoPerfil(Perfil.CORREDOR);
        String senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));
        return repository.save(usuario);
    }


    public List<Usuarios> findAll() {
        return repository.findAll();
    }

}
