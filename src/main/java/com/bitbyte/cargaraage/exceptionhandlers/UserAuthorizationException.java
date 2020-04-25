package com.bitbyte.cargaraage.exceptionhandlers;

import lombok.Getter;

@Getter
public class UserAuthorizationException extends RuntimeException {
    private final String message;

    public UserAuthorizationException(String message) {
        super(message);
        this.message = message;
    }
}