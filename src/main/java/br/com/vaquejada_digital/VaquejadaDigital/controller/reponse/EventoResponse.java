package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Status;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record EventoResponse(
        Long id,
        String nome,
        LocalDate dataInicio,
        LocalDate dataFim,
        String local,
        String descricao,
        BigDecimal precoBaseSenha,
        Integer quantidadeTotalSenhas,
        Integer senhasVendidas,
        Integer senhasDisponiveis,
        List<String> imagensVideos,
        Status status,
        List<String> juizes,
        List<String> locutores
){}
