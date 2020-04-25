package com.bitbyte.cargaraage.exceptionhandlers;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}