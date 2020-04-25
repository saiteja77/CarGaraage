package com.bitbyte.cargaraage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "OAuth2ClientDetails")
@JsonIgnoreProperties(value = {"validity", "clientSecret", "client_id"}, allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OAuth2ClientDetails {
    @Id
    @JsonProperty(value = "client_id")
    private String id;
    private String clientSecret;
    private String resourceName;
    private List<String> grantedAuthorities;
    @JsonProperty(value = "validity")
    private String accessTokenValidity;
    private String grantType;
    @JsonIgnore
    private byte[] salt;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastUpdateDate;
    private User createdBy;
    private User lastUpdatedBy;
}