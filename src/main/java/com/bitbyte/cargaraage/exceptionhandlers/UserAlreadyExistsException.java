package com.bitbyte.cargaraage.exceptionhandlers;

public class UserAlreadyExistsException extends RuntimeException {

    private String message;

    public UserAlreadyExistsException(String user) {
        this.message = "User " + user + " already exists. Try to sign in. If you have forgot your password, reset it.";
    }
}
