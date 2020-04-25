package com.bitbyte.cargaraage.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "makes")
public class Make {
    @Id
    private String id;
    private String name;
    private Integer establishedYear;
    private String headQuarters;
}