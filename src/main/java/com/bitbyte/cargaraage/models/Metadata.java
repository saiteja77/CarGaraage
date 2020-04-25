package com.bitbyte.cargaraage.models;

import com.bitbyte.cargaraage.entities.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Metadata {
    private String name;
    private String description;
    private Risk itemRisk;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
    @DBRef(db = "car_garaage")
    private User createdBy;
    @DBRef(db = "car_garaage")
    private User lastUpdateBy;
    private boolean requestable;
    private boolean deleted;
}