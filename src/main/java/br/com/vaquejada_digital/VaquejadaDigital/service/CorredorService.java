package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Corredor;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.repository.CorredorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorredorService {

    private final CorredorRepository corredorRepository;

    public Corredor criarCorredorVazio(Usuarios usuario) {
        Optional<Corredor> corredorExistente = corredorRepository.findByUsuarioId(usuario.getId());
        if (corredorExistente.isPresent()) {
            return corredorExistente.get();
        }

        Corredor corredor = Corredor
                .builder()
                .usuario(usuario)
                .criadoEm(LocalDateTime.now())
                .ativo(true)
                .build();
        return corredorRepository.save(corredor);
    }

    public Optional<Corredor> buscarPorUsuarioId(Long usuarioId) {
        return corredorRepository.findByUsuarioId(usuarioId);
    }

    public Corredor atualizarPerfil(Long id, Corredor corredor) {
        Corredor atualizarCorredor = corredorRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Corredor não encontrado."));

        atualizarCorredor.setNomeCompleto(corredor.getNomeCompleto());
        atualizarCorredor.setApelido(corredor.getApelido());
        atualizarCorredor.setCpf(corredor.getCpf());
        atualizarCorredor.setCidade(corredor.getCidade());
        atualizarCorredor.setFotoPerfil(corredor.getFotoPerfil());
        atualizarCorredor.setTelefone(corredor.getTelefone());
        corredorRepository.save(atualizarCorredor);
        return atualizarCorredor;
    }


}
