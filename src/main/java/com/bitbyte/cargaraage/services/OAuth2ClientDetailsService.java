package com.bitbyte.cargaraage.services;

import com.bitbyte.cargaraage.configurations.OAuth2Config;
import com.bitbyte.cargaraage.entities.OAuth2ClientDetails;
import com.bitbyte.cargaraage.entities.User;
import com.bitbyte.cargaraage.exceptionhandlers.InvalidCredentialsException;
import com.bitbyte.cargaraage.exceptionhandlers.InvalidOAuth2ClientDetailsException;
import com.bitbyte.cargaraage.exceptionhandlers.ResourceNotFoundException;
import com.bitbyte.cargaraage.models.TokenResponse;
import com.bitbyte.cargaraage.repositories.GroupsRepository;
import com.bitbyte.cargaraage.repositories.OAuth2ClientDetailsRepository;
import com.bitbyte.cargaraage.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bitbyte.cargaraage.utilities.PasswordUtils.*;
import static com.bitbyte.cargaraage.utilities.SecurityUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.random;

@Service
public class OAuth2ClientDetailsService {

    private final OAuth2ClientDetailsRepository oAuth2ClientDetailsRepository;
    private final GroupsRepository groupsRepository;
    private final UsersRepository usersRepository;
    private final OAuth2Config oAuth2Config;

    @Value("${hashing-algorithm}")
    private String hashingAlgorithm;

    @Autowired
    public OAuth2ClientDetailsService(OAuth2ClientDetailsRepository oAuth2ClientDetailsRepository, GroupsRepository groupsRepository, UsersRepository usersRepository, OAuth2Config oAuth2Config) {
        this.oAuth2ClientDetailsRepository = oAuth2ClientDetailsRepository;
        this.groupsRepository = groupsRepository;
        this.usersRepository = usersRepository;
        this.oAuth2Config = oAuth2Config;
    }

    public OAuth2ClientDetails createOAuth2Client(OAuth2ClientDetails oAuth2ClientDetails, String bearerToken) throws NoSuchAlgorithmException {
        validateOAuth2ClientDetailsAreNonEmpty(oAuth2ClientDetails);
        try {
            Integer.parseInt(oAuth2ClientDetails.getAccessTokenValidity());
        } catch (NumberFormatException e) {
            throw new InvalidOAuth2ClientDetailsException("Value for validity should be a number(hours)");
        }
        if (oAuth2ClientDetails.getGrantedAuthorities().size() > 0) {
            oAuth2ClientDetails
                    .getGrantedAuthorities()
                    .forEach(authority -> groupsRepository
                            .findByMetadata_Name(authority)
                            .orElseThrow(() -> new InvalidOAuth2ClientDetailsException("Invalid authority: " + authority))
                    );
        }
        byte[] salt = createSalt();
        String secret = random(12, true, true);
        byte[] saltedHashPassword = generateSaltedHash(secret, hashingAlgorithm, salt);
        oAuth2ClientDetails.setClientSecret(convertByteArrayToHexString(saltedHashPassword));
        oAuth2ClientDetails.setGrantType("password");
        oAuth2ClientDetails.setSalt(salt);
        User requestedUser = getUser(bearerToken);
        oAuth2ClientDetails.setCreatedBy(requestedUser);
        oAuth2ClientDetails.setLastUpdatedBy(requestedUser);
        OAuth2ClientDetails savedOAuth2ClientDetails = oAuth2ClientDetailsRepository.save(oAuth2ClientDetails);
        savedOAuth2ClientDetails.setClientSecret(secret);
        return savedOAuth2ClientDetails;
    }

    private void validateOAuth2ClientDetailsAreNonEmpty(OAuth2ClientDetails oAuth2ClientDetails) {
        if (oAuth2ClientDetails.getAccessTokenValidity() == null
                || oAuth2ClientDetails.getAccessTokenValidity().isEmpty()
                || oAuth2ClientDetails.getResourceName() == null
                || !oAuth2ClientDetails.getResourceName().isEmpty()
        ) {
            throw new InvalidOAuth2ClientDetailsException("validity and resource_name cannot be empty");
        }
    }

    private User getUser(String bearerToken) {
        return usersRepository
                .findByUserName(getUserName(removeBearerString(bearerToken)))
                .get();
    }

    public List<OAuth2ClientDetails> getOAuth2Clients(String clientId, String userName) {

        if ((clientId != null && !clientId.isEmpty()) && !(userName != null && !userName.isEmpty())) {
            // clientId is not empty
            List<OAuth2ClientDetails> oAuth2ClientDetailsList = new ArrayList<>();
            Optional<OAuth2ClientDetails> oAuth2ClientDetails = oAuth2ClientDetailsRepository.findById(clientId);
            oAuth2ClientDetails.orElseThrow(() -> new ResourceNotFoundException("client_id not found"));
            oAuth2ClientDetails.get().setClientSecret(null);
            oAuth2ClientDetailsList.add(oAuth2ClientDetails.get());
            return oAuth2ClientDetailsList;
        } else if (!(clientId != null && !clientId.isEmpty()) && (userName != null && !userName.isEmpty())) {
            List<OAuth2ClientDetails> oAuth2ClientDetailsList = oAuth2ClientDetailsRepository.findByCreatedBy_EmailAddressOrCreatedBy_UserName(userName, userName);
            if (oAuth2ClientDetailsList.size() > 0) {
                oAuth2ClientDetailsList.forEach(detail -> detail.setClientSecret(null));
                return oAuth2ClientDetailsList;
            } else
                throw new ResourceNotFoundException("No clients found");
        } else {
            throw new InvalidOAuth2ClientDetailsException("Either client_id or user_name should have values");
        }
    }

    public TokenResponse generateToken(OAuth2ClientDetails clientDetails) throws NoSuchAlgorithmException {
        if (clientDetails.getId() != null && clientDetails.getClientSecret() != null) {
            Optional<OAuth2ClientDetails> oAuth2ClientDetails = oAuth2ClientDetailsRepository.findById(clientDetails.getId());
            if (oAuth2ClientDetails.isPresent()) {
                OAuth2ClientDetails client = oAuth2ClientDetails.get();
                boolean validCredentials = comparePasswords(clientDetails.getClientSecret(), client.getClientSecret(), client.getSalt(), hashingAlgorithm);
                if (validCredentials) {
                    return generateBearerToken(clientDetails, oAuth2Config);
                } else throw new InvalidCredentialsException();
            } else throw new InvalidCredentialsException();
        } else throw new InvalidCredentialsException();
    }
}