package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario save(Usuario usuario) {

        String password = usuario.getSenha();
        usuario.setSenha(passwordEncoder.encode(password));
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}
