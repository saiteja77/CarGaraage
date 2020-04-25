package com.bitbyte.cargaraage.repositories;

import com.bitbyte.cargaraage.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAddress(String emailAddress);
    Optional<User> findByUserNameOrEmailAddressIgnoreCase(String userName, String emailAddress);
    List<User> findAllByIdIn(List<String> userIds);
    Optional<User> findByUserName(String userName);
}