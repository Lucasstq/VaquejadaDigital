package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Corredor;
import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.repositories.CorredorRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CorredorService {

    private final CorredorRepository corredorRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Corredor criar(Long usuarioId, Corredor corredor) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (corredorRepository.findByUsuarioId(usuarioId).isPresent()) {
            throw new RuntimeException("Corredor já cadastrado para este usuário");
        }

        if (corredor.getCpf() != null && corredorRepository.existsByCpf(corredor.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        corredor.setUsuario(usuario);
        return corredorRepository.save(corredor);
    }

    @Transactional
    public Corredor buscarPorId(Long id) {
        return corredorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corredor não encontrado"));
    }

    @Transactional
    public Corredor buscarPorUsuarioId(Long usuarioId) {
        return corredorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Corredor não encontrado"));
    }

    @Transactional
    public List<Corredor> listarTodos() {
        return corredorRepository.findAll();
    }

    @Transactional
    public Corredor atualizar(Long id, Corredor corredorAtualizado) {
        Corredor corredor = buscarPorId(id);

        if (corredorAtualizado.getCpf() != null && !corredorAtualizado.getCpf().equals(corredor.getCpf())) {
            if (corredorRepository.existsByCpf(corredorAtualizado.getCpf())) {
                throw new RuntimeException("CPF já cadastrado");
            }
            corredor.setCpf(corredorAtualizado.getCpf());
        }

        corredor.setApelido(corredorAtualizado.getApelido());
        corredor.setTelefone(corredorAtualizado.getTelefone());
        corredor.setCidade(corredorAtualizado.getCidade());
        corredor.setEstado(corredorAtualizado.getEstado());
        corredor.setFotoUrl(corredorAtualizado.getFotoUrl());
        corredor.setDataNascimento(corredorAtualizado.getDataNascimento());

        return corredorRepository.save(corredor);
    }
}
