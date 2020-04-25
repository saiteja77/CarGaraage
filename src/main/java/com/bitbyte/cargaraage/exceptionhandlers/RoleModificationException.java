package com.bitbyte.cargaraage.exceptionhandlers;

public class RoleModificationException extends RuntimeException {
    private final String message;

    public RoleModificationException(String message) {
        super(message);
        this.message = message;
    }
}