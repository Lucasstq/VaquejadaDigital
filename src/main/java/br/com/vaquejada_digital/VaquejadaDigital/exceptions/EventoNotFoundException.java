package br.com.vaquejada_digital.VaquejadaDigital.exceptions;

public class EventoNotFoundException extends RuntimeException {
    public EventoNotFoundException(String message) {
        super(message);
    }
}
