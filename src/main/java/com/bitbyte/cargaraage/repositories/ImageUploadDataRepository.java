package com.bitbyte.cargaraage.repositories;

import com.bitbyte.cargaraage.entities.ImageUploadData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ImageUploadDataRepository extends MongoRepository<ImageUploadData, String> {
    List<ImageUploadData> findByUsedAndCreatedDateIsLessThanEqual(boolean used, Date date);
}
