package com.bitbyte.cargaraage.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenResponse {
    private final String accessToken;
    private final String tokenType;
    private final Date validTill;
}
