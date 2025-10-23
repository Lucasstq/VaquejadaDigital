package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.response.CategoriaSenhasResponse;
import com.br.vaquejadadigital.dtos.response.MapaSenhasResponse;
import com.br.vaquejadadigital.dtos.response.SenhaMapaResponse;
import com.br.vaquejadadigital.dtos.response.SenhaResponse;
import com.br.vaquejadadigital.entities.Senha;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class SenhaMapper {

    public SenhaResponse toResponse(Senha senha) {
        return SenhaResponse
                .builder()
                .id(senha.getId())
                .eventoId(senha.getEvento().getId())
                .nomeEvento(senha.getEvento().getNome())
                .categoriaId(senha.getCategoria().getId())
                .nomeCategoria(String.valueOf(senha.getCategoria().getNome()))
                .numeroSenha(senha.getNumeroSenha())
                .status(senha.getStatus())
                .valor(senha.getValor())
                .dataCriacao(senha.getDataCriacao())
                .build();
    }

    public SenhaMapaResponse toMapa(Senha senha) {
        String nomeDupla = senha.getDupla() != null ?
                (senha.getDupla().getNomeDupla() != null ?
                        senha.getDupla().getNomeDupla() :
                        senha.getDupla().getPuxador().getUsuario().getNome() + " / " +
                                senha.getDupla().getEsteireiro().getUsuario().getNome()
                ) : null;

        return new SenhaMapaResponse(
                senha.getId(),
                senha.getNumeroSenha(),
                senha.getStatus().name(),
                nomeDupla
        );
    }

    public MapaSenhasResponse toMapaSenhas(List<Senha> senhas, String nomeEvento, Long eventoId) {
        Map<Long, List<Senha>> senhasPorCategoria = senhas.stream()
                .collect(Collectors.groupingBy(s -> s.getCategoria().getId()));

        List<CategoriaSenhasResponse> categorias = senhasPorCategoria.entrySet().stream()
                .map(entry -> {
                    Senha primeiraSenha = entry.getValue().get(0);
                    List<SenhaMapaResponse> senhasMapa = entry.getValue().stream()
                            .map(senha -> toMapa(senha))
                            .collect(Collectors.toList());

                    return new CategoriaSenhasResponse(
                            primeiraSenha.getCategoria().getId(),
                            primeiraSenha.getCategoria().getNome().getDescricao(),
                            senhasMapa
                    );
                })
                .collect(Collectors.toList());

        return new MapaSenhasResponse(eventoId, nomeEvento, categorias);
    }

}
