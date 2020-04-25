package com.bitbyte.cargaraage.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "authorize")
@Getter
@Setter
public class AuthorizationGroups {
    private String readGroups;
    private String modifyGroups;
    private String createGroups;
    private String readRoles;
    private String modifyRoles;
    private String createRoles;
    private String readUsers;
}