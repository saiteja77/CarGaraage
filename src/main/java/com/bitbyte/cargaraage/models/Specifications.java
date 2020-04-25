package com.bitbyte.cargaraage.models;

import com.bitbyte.cargaraage.entities.Color;
import com.bitbyte.cargaraage.entities.Make;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specifications {
    private String bodyStyle;
    @DBRef
    private Make make;
    private String fuelType;
    private String fuelCapacity;
    private String accelerationTime;
    private String seatingCapacity;
    private String engineType;
    private Color interiorColor;
    private Color exteriorColor;
    private String transmission;
    private String driveTerrain;
    private Integer year;
    private Integer power;
    private Integer torque;

}
