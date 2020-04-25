package com.bitbyte.cargaraage.models;

import lombok.Data;

@Data
public class DecodedToken {
    private final String header;
    private final String payload;
    private boolean validExpiration;
    private boolean validIssuer;
    private boolean authorized;
}
