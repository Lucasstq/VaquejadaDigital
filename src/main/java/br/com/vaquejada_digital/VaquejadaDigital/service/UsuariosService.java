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
    private final CorredorService corredorService;

    public Usuarios criarUsuario(Usuarios usuario) {

        String senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));

        Usuarios save = repository.save(usuario);
        if (save.getTipoPerfil() == Perfil.CORREDOR) {
            corredorService.criarCorredorVazio(save);
        }
        return save;

    }

    public Usuarios promoverUsuario(Long id, Perfil novoPerfil) {
        Usuarios usuarios = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        if (usuarios.getTipoPerfil() == Perfil.ADMIN) {
            throw new RuntimeException("Não é possível alterar perfil de admin");
        }

        usuarios.setTipoPerfil(novoPerfil);
        Usuarios salvo = repository.save(usuarios);
        return salvo;
    }

    public List<Usuarios> findAll() {
        return repository.findAll();
    }


    public List<Usuarios> buscarJuizesById(List<Long> id) {
        List<Usuarios> juizes = repository.findByIdInAndTipoPerfil(id, Perfil.JUIZ);

        if (juizes.size() != id.size()) {
            throw new IllegalArgumentException("Alguns IDs não correspondem a juízes válidos");
        }

        return juizes;
    }

    public List<Usuarios> buscarLocutoresById(List<Long> id) {
        List<Usuarios> locutores = repository.findByIdInAndTipoPerfil(id, Perfil.LOCUTOR);

        if (locutores.size() != id.size()) {
            throw new IllegalArgumentException("Alguns IDs não correspondem a locutores válidos");
        }

        return locutores;
    }

}
