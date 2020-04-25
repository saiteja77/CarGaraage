package com.bitbyte.cargaraage.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.jwt")
@Getter
@Setter
public class OAuth2Config {
    private String issuerUri;
    private String jwsAlgorithm;
    private String publicKeyLocation;
}