package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Senha;
import com.br.vaquejadadigital.entities.enums.StatusSenha;
import com.br.vaquejadadigital.repositories.EventoRepository;
import com.br.vaquejadadigital.repositories.SenhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SenhaService {

    private final SenhaRepository senhaRepository;
    private final EventoRepository eventoRepository;

    @Transactional(readOnly = true)
    public List<Senha> listarPorEvento(Long eventoId) {
        return senhaRepository.findByEventoId(eventoId);
    }

    @Transactional(readOnly = true)
    public List<Senha> listarPorEventoECategoria(Long eventoId, Long categoriaId) {
        return senhaRepository.findByEventoIdAndCategoriaId(eventoId, categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Senha> listarDisponiveis(Long eventoId) {
        return senhaRepository.findByEventoIdAndStatus(eventoId, StatusSenha.DISPONIVEL);
    }

    @Transactional(readOnly = true)
    public Senha buscarPorId(Long id) {
        return senhaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Senha não encontrada"));
    }

    @Transactional
    public Senha bloquearSenha(Long id) {
        Senha senha = buscarPorId(id);

        if (senha.getStatus() == StatusSenha.VENDIDA) {
            throw new RuntimeException("Senha já vendida não pode ser bloqueada");
        }

        senha.setStatus(StatusSenha.BLOQUEADA);
        return senhaRepository.save(senha);
    }

    @Transactional
    public Senha liberarSenha(Long id) {
        Senha senha = buscarPorId(id);

        if (senha.getStatus() != StatusSenha.BLOQUEADA) {
            throw new RuntimeException("Apenas senhas bloqueadas podem ser liberadas");
        }

        senha.setStatus(StatusSenha.DISPONIVEL);
        return senhaRepository.save(senha);
    }
}
