package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Status;

public record EventoUpdateStatusRequest(Status status) {
}
