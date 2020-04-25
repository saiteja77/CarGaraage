package com.bitbyte.cargaraage.entities;

import com.bitbyte.cargaraage.models.Specifications;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cars")
public class Car {
    @Id
    private String id;
    private String name;
    private Long price;
    private Specifications specs;
    private String tags;
    @DBRef
    private List<ImageUploadData> images;
}
