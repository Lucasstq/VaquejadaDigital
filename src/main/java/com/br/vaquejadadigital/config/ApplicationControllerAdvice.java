package com.br.vaquejadadigital.config;

import com.br.vaquejadadigital.exception.BusinessException;
import com.br.vaquejadadigital.exception.ResourceNotFoundException;
import com.br.vaquejadadigital.exception.UsernameOrPasswordInvalid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    // Exeção caso o email seja fornecido e já exista.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("email")) {
            return new ResponseEntity<>("O email fornecido já está em uso. ", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Errro de integridade de dados, verifique novamente. ", HttpStatus.BAD_REQUEST);
    }

    //Exeção para usuario(email) ou senha
    @ExceptionHandler(UsernameOrPasswordInvalid.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUsernameOrPasswordInvalid(UsernameOrPasswordInvalid ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->
                errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessException(BusinessException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex) {
        return "Erro interno do servidor: " + ex.getMessage();
    }

}
