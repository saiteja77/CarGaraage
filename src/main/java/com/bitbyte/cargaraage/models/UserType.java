package com.bitbyte.cargaraage.models;

public enum UserType {
    User, ServiceAccount;

    @Override
    public String toString() {
        switch (this) {
            case User:
                return "User";
            case ServiceAccount:
                return "ServiceAccount";
            default:
                return "";
        }
    }
}
