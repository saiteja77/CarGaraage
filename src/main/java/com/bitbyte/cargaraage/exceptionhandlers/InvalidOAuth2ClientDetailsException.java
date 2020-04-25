package com.bitbyte.cargaraage.exceptionhandlers;

public class InvalidOAuth2ClientDetailsException extends RuntimeException {
    public InvalidOAuth2ClientDetailsException(String message) {
        super(message);
    }
}
