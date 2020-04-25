package com.bitbyte.cargaraage.models;

public enum UserStatus {
    Active(1) , Locked(0), Disabled(-1);

    private int status;

    UserStatus(int status) {
        this.status = status;
    }

    public UserStatus getStatus() {
        return this;
    }
}