package com.bitbyte.cargaraage.exceptionhandlers;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
