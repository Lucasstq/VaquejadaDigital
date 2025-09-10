package br.com.sistemavaquejada.vaquejada_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String message) {
        super(message);
    }

}
