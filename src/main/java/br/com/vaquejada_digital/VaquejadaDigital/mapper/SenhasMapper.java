package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.SenhaResponse;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Senha;

public class SenhasMapper {
    public static SenhaResponse from(Senha senha) {
        return SenhaResponse.builder()
                .id(senha.getId())
                .numeroSenha(senha.getNumeroSenha())
                .disponivel(senha.isDisponivel())
                .bloqueada(senha.getBloqueada())
                .diaCorrida(senha.getDiaCorrida())
                .valorPago(senha.getValorPago())
                .pagouComDesconto(senha.getPagouComDesconto())
                .nomeDupla(senha.getDupla() != null ?
                        senha.getDupla().getNomeDuplaFormatado() : null)
                .statusPagamento(senha.getStatusPagamento().name())
                .build();
    }
}
