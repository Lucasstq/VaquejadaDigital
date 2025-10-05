package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

public record LoginResponse(String token, String refreshToken, Long expiresIn) {
}
