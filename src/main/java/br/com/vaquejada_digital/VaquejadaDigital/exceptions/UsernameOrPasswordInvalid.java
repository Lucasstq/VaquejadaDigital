package br.com.vaquejada_digital.VaquejadaDigital.exceptions;

public class UsernameOrPasswordInvalid extends RuntimeException {
    public UsernameOrPasswordInvalid(String message) {
        super(message);
    }
}
