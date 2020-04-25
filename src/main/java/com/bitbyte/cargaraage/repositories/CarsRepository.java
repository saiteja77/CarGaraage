package com.bitbyte.cargaraage.repositories;

import com.bitbyte.cargaraage.entities.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarsRepository extends MongoRepository<Car, String> {
}