package com.bitbyte.cargaraage.controllers;

import com.bitbyte.cargaraage.configurations.OAuth2Config;
import com.bitbyte.cargaraage.entities.OAuth2ClientDetails;
import com.bitbyte.cargaraage.models.TokenResponse;
import com.bitbyte.cargaraage.services.OAuth2ClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.bitbyte.cargaraage.utilities.SecurityUtils.isAuthorized;

@RestController
public class OAuth2ClientDetailsController {

    private final OAuth2ClientDetailsService oAuth2ClientDetailsService;
    private final OAuth2Config oAuth2Config;

    @Autowired
    public OAuth2ClientDetailsController(OAuth2ClientDetailsService oAuth2ClientDetailsService, OAuth2Config oAuth2Config) {
        this.oAuth2ClientDetailsService = oAuth2ClientDetailsService;
        this.oAuth2Config = oAuth2Config;
    }

    @PostMapping("/auth/oauth/v2/client")
    public OAuth2ClientDetails createOAuth2Client(@RequestHeader("Authorization") String bearerToken,
                                                  @RequestBody OAuth2ClientDetails oAuth2ClientDetails) throws NoSuchAlgorithmException {
        isAuthorized(bearerToken, "APP-OAUTH2-Client-Creator", oAuth2Config);
        return oAuth2ClientDetailsService.createOAuth2Client(oAuth2ClientDetails, bearerToken);
    }

    @GetMapping("/auth/oauth/v2/client")
    public List<OAuth2ClientDetails> getOAuth2Clients(@RequestHeader("Authorization") String bearerToken, String clientId, String userName) {
        isAuthorized(bearerToken, "APP-OAUTH2-Client-Viewer", oAuth2Config);
        return oAuth2ClientDetailsService.getOAuth2Clients(clientId, userName);
    }

//    @PostMapping("/auth/oauth/v2/client")
//    public TokenResponse getBearerToken(@RequestBody OAuth2ClientDetails oAuth2ClientDetails) throws NoSuchAlgorithmException {
//        return oAuth2ClientDetailsService.generateToken(oAuth2ClientDetails);
//    }
}
