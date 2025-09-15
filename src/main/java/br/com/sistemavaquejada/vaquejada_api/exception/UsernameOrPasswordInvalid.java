package br.com.sistemavaquejada.vaquejada_api.exception;

public class UsernameOrPasswordInvalid extends RuntimeException {
    public UsernameOrPasswordInvalid(String message) {
        super(message);
    }
}
