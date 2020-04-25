package com.bitbyte.cargaraage.exceptionhandlers;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid Credentials");
    }
}
