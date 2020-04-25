package com.bitbyte.cargaraage.repositories;

import com.bitbyte.cargaraage.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RolesRepository extends MongoRepository<Role, String> {
}
