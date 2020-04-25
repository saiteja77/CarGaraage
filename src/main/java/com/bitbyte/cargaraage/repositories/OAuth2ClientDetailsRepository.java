package com.bitbyte.cargaraage.repositories;

import com.bitbyte.cargaraage.entities.OAuth2ClientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OAuth2ClientDetailsRepository extends MongoRepository<OAuth2ClientDetails, String> {

    List<OAuth2ClientDetails> findByCreatedBy_EmailAddressOrCreatedBy_UserName(String emailAddress, String userName);
}