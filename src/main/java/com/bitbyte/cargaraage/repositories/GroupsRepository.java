package com.bitbyte.cargaraage.repositories;

import com.bitbyte.cargaraage.entities.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupsRepository extends MongoRepository<Group, String> {
    Optional<Group> findByMetadata_Name(String name);
}
