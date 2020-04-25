package com.bitbyte.cargaraage.exceptionhandlers;

public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }
}
