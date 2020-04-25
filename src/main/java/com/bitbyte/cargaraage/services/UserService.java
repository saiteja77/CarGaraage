package com.bitbyte.cargaraage.services;

import com.bitbyte.cargaraage.configurations.OAuth2Config;
import com.bitbyte.cargaraage.entities.User;
import com.bitbyte.cargaraage.exceptionhandlers.InvalidCredentialsException;
import com.bitbyte.cargaraage.exceptionhandlers.UserAlreadyExistsException;
import com.bitbyte.cargaraage.models.Credentials;
import com.bitbyte.cargaraage.models.TokenResponse;
import com.bitbyte.cargaraage.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.bitbyte.cargaraage.utilities.PasswordUtils.*;
import static com.bitbyte.cargaraage.utilities.SecurityUtils.generateToken;

@Service
@Slf4j
public class UserService {

    private final UsersRepository repository;
    private final OAuth2Config oAuth2Config;

    @Value("${hashing-algorithm}")
    private String hashingAlgorithm;

    @Autowired
    public UserService(UsersRepository repository, OAuth2Config oAuth2Config) {
        this.repository = repository;
        this.oAuth2Config = oAuth2Config;
    }

    public TokenResponse signUpNewUser(User user) throws NoSuchAlgorithmException {
        Optional optionalUser = repository.findByEmailAddress(user.getEmailAddress());

        optionalUser.ifPresent(u -> { throw new UserAlreadyExistsException(user.getEmailAddress()); });

        byte[] salt = createSalt();
        byte[] saltedHashPassword = generateSaltedHash(user.getPassword(), hashingAlgorithm, salt);
        user.setPassword(convertByteArrayToHexString(saltedHashPassword));
        user.setSalt(salt);
        User savedUser = repository.save(user);
        return generateToken(savedUser, oAuth2Config);
    }

    public TokenResponse authenticateUser(Credentials credentials) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = repository.findByUserNameOrEmailAddressIgnoreCase(credentials.getUserName(), credentials.getUserName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean isValidUser = comparePasswords(credentials.getPassword(), user.getPassword(), user.getSalt(), hashingAlgorithm);
            if (isValidUser) return generateToken(optionalUser.get(), oAuth2Config);
            else throw new InvalidCredentialsException();
        } else
            throw new InvalidCredentialsException();
    }
}