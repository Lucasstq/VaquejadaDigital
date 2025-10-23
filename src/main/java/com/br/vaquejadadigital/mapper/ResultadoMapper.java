package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.ResultadoRequest;
import com.br.vaquejadadigital.dtos.response.ResultadoResponse;
import com.br.vaquejadadigital.entities.OrdemCorrida;
import com.br.vaquejadadigital.entities.Resultado;
import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.repositories.OrdemCorridaRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResultadoMapper {

    private final OrdemCorridaRepository ordemCorridaRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    public Resultado toEntity(ResultadoRequest request) {
        OrdemCorrida ordemCorrida = ordemCorridaRepository.findById(request.ordemCorridaId())
                .orElseThrow(() -> new RuntimeException("Ordem de corrida não encontrada"));

        // Valida se já existe resultado para essa ordem
        if (ordemCorrida.getResultado() != null) {
            throw new RuntimeException("Resultado já registrado para esta corrida");
        }

        // Pega o juiz logado
        Usuario juiz = getUsuarioLogado();

        return Resultado.builder()
                .ordemCorrida(ordemCorrida)
                .tipoResultado(request.tipoResultado())
                .observacao(request.observacao())
                .tempoCorrida(request.tempoCorrida())
                .juiz(juiz)
                .build();
    }

    public ResultadoResponse toResponse(Resultado resultado) {
        // Monta o nome da dupla
        String nomeDupla = resultado.getOrdemCorrida().getDupla().getNomeDupla() != null ?
                resultado.getOrdemCorrida().getDupla().getNomeDupla() :
                resultado.getOrdemCorrida().getDupla().getPuxador().getUsuario().getNome() + " / " +
                        resultado.getOrdemCorrida().getDupla().getEsteireiro().getUsuario().getNome();

        return new ResultadoResponse(
                resultado.getId(),
                resultado.getOrdemCorrida().getId(),
                nomeDupla,
                resultado.getTipoResultado(),
                resultado.getObservacao(),
                resultado.getJuiz().getNome(),
                resultado.getTempoCorrida(),
                resultado.getDataRegistro()
        );
    }
}
