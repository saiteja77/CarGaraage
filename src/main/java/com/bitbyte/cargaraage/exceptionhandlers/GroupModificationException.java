package com.bitbyte.cargaraage.exceptionhandlers;

public class GroupModificationException extends RuntimeException {
    private final String message;

    public GroupModificationException(String message) {
        super(message);
        this.message = message;
    }
}
