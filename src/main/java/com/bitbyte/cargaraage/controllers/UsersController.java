package com.bitbyte.cargaraage.controllers;

import com.bitbyte.cargaraage.entities.User;
import com.bitbyte.cargaraage.exceptionhandlers.UserAlreadyExistsException;
import com.bitbyte.cargaraage.exceptionhandlers.InvalidCredentialsException;
import com.bitbyte.cargaraage.models.Credentials;
import com.bitbyte.cargaraage.models.TokenResponse;
import com.bitbyte.cargaraage.repositories.UsersRepository;
import com.bitbyte.cargaraage.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@Slf4j
public class UsersController {

    private final UserService service;

    @Autowired
    private UsersRepository repository;

    @Autowired
    public UsersController(UserService service) {
        this.service = service;
    }

    @PostMapping("/car_garaage/v1/users/register")
    public TokenResponse createNewUser(@RequestBody User user) {
        try {
            return service.signUpNewUser(user);
        } catch (NoSuchAlgorithmException e) {
            log.error("UsersController :: createNewUser: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was something bad on our side. Apologies for any inconvenience.");
        } catch (UserAlreadyExistsException e) {
            log.error("UsersController :: createNewUser: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/car_garaage/v1/auth/oauth/v2/login")
    public TokenResponse authenticateUser(@RequestBody Credentials credentials) {
        try {
            return service.authenticateUser(credentials);
        } catch (NoSuchAlgorithmException e) {
            log.error("UsersController :: authenticateUser: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was something bad on our side. Apologies for any inconvenience.");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return repository.findAll();
    }
}
