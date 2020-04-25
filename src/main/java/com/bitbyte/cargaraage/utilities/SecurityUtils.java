package com.bitbyte.cargaraage.utilities;

import com.bitbyte.cargaraage.configurations.OAuth2Config;
import com.bitbyte.cargaraage.entities.Group;
import com.bitbyte.cargaraage.entities.OAuth2ClientDetails;
import com.bitbyte.cargaraage.entities.Role;
import com.bitbyte.cargaraage.entities.User;
import com.bitbyte.cargaraage.exceptionhandlers.AccessDeniedException;
import com.bitbyte.cargaraage.exceptionhandlers.InternalServerException;
import com.bitbyte.cargaraage.exceptionhandlers.UserAuthorizationException;
import com.bitbyte.cargaraage.models.DecodedToken;
import com.bitbyte.cargaraage.models.Metadata;
import com.bitbyte.cargaraage.models.TokenResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.claims;
import static java.util.stream.Collectors.toList;
import static org.springframework.security.jwt.JwtHelper.decode;

@Slf4j
public class SecurityUtils {

    public static TokenResponse generateToken(User authenticatedUser, OAuth2Config oAuth2Config) {
        Claims claims = claims()
                .setSubject(authenticatedUser.getUserName())
                .setExpiration(DateUtils.addHours(new Date(), 8))
                .setIssuedAt(new Date())
                .setIssuer(oAuth2Config.getIssuerUri());
        claims.put("user_id", authenticatedUser.getId());
        claims.put("user_name", authenticatedUser.getUserName());
        claims.put("email", authenticatedUser.getEmailAddress());
        claims.put("first_name", authenticatedUser.getFirstName());
        claims.put("last_name", authenticatedUser.getLastName());
        claims.put("full_name", authenticatedUser.getFirstName() + " " + authenticatedUser.getLastName());
        claims.put("status", authenticatedUser.getStatus());

        List<String> groups = new ArrayList<>();

        if (authenticatedUser.getGroups() != null) {
            groups.addAll(authenticatedUser
                    .getGroups()
                    .stream()
                    .map(Group::getMetadata)
                    .map(Metadata::getName)
                    .collect(toList())
            );
        }
        if (authenticatedUser.getRoles() != null) {
            groups.addAll(authenticatedUser
                    .getRoles()
                    .stream()
                    .map(Role::getGroups)
                    .flatMap(List::stream)
                    .map(Group::getMetadata)
                    .map(Metadata::getName)
                    .collect(toList())
            );
        }

        claims.put("groups", groups);

        return new TokenResponse(builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, authenticatedUser.getPassword())
                .compact(), "id-token", claims.getExpiration());
    }

    public static TokenResponse generateBearerToken(OAuth2ClientDetails oAuth2ClientDetails, OAuth2Config oAuth2Config) {
        Date issuedAt = new Date();
        Claims claims = claims()
                .setSubject(oAuth2ClientDetails.getId())
                .setExpiration(DateUtils.addHours(issuedAt, Integer.parseInt(oAuth2ClientDetails.getAccessTokenValidity())))
                .setIssuedAt(issuedAt)
                .setIssuer(oAuth2Config.getIssuerUri());

        return new TokenResponse(builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, oAuth2ClientDetails.getClientSecret())
                .compact(), "access-token", claims.getExpiration());
    }

    public static String getUserName(String token) {
        DecodedToken decodedToken = new DecodedToken(
                decode(token)
                .toString(), decode(token)
                .getClaims()
        );
        return new JsonParser()
                .parse(decodedToken.getPayload())
                .getAsJsonObject()
                .get("userName")
                .getAsString();
    }

    public static void isAuthorized(String token, String group, OAuth2Config oAuth2Config) {
        DecodedToken decodedToken = new DecodedToken(decode(token).toString(), decode(token).getClaims());
        validateTokenExpiration(decodedToken);
        if (!decodedToken.isValidExpiration())
            throw new UserAuthorizationException("Token has expired, generate a new one");
        validateIssuer(decodedToken, oAuth2Config.getIssuerUri());
        if (!decodedToken.isValidIssuer())
            throw new UserAuthorizationException("Invalid token issuer");
        try {
            JsonObject jsonObject = new JsonParser().parse(decodedToken.getPayload()).getAsJsonObject();
            JsonArray groups = jsonObject.get("groups").getAsJsonArray();
            if (groups.size() > 0)
            for (JsonElement jsonElement : groups) {
                if (jsonElement.getAsString().equalsIgnoreCase(group))
                    return;
            }
        } catch (Exception e){
            log.error("SecurityUtils :: isAuthorized: {}", e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
        throw new AccessDeniedException("You are not authorized to request this resource");
    }

    private static void validateIssuer(DecodedToken decodedToken, String issuerUri) {
        try {
            JsonObject jsonObject = new JsonParser().parse(decodedToken.getPayload()).getAsJsonObject();
            if(jsonObject.get("iss").getAsString().equals(issuerUri)){
                decodedToken.setValidIssuer(true);
            } else
                decodedToken.setValidIssuer(false);
        } catch (Exception e){
            log.error(e.getMessage());
            decodedToken.setValidIssuer(false);
        }
    }

    private static void validateTokenExpiration(DecodedToken decodedToken) {
        try {
            JsonObject jsonObject = new JsonParser().parse(decodedToken.getPayload()).getAsJsonObject();
            if(isExpirationValidated(jsonObject.get("exp").getAsLong())){
                decodedToken.setValidExpiration(true);
            } else {
                decodedToken.setValidExpiration(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isExpirationValidated(Long dateToCheckInMillis) {
        return System.currentTimeMillis() < (dateToCheckInMillis.longValue() * 1000);
    }

    public static String removeBearerString(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }
}
