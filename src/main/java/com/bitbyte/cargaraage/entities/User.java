package com.bitbyte.cargaraage.entities;

import com.bitbyte.cargaraage.models.UserStatus;
import com.bitbyte.cargaraage.models.UserType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(allowSetters = true, value = "password")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;
    private UserStatus status;
    private UserType userType;
    private String gender;
    @JsonIgnore
    private byte[] salt;
    private String password;
    private String dob;
    @DBRef(db = "car_garaage", lazy = true)
    @JsonBackReference("roles")
    private List<Role> roles;
    @DBRef(db = "car_garaage", lazy = true)
    @JsonBackReference("groups")
    private List<Group> groups;

    public void addGroup(Group group) {
        initializeGroups();
        groups.add(group);
    }

    public void addGroups(List<Group> groups) {
        initializeGroups();
        this.groups.addAll(groups);
    }

    public void addRole(Role role) {
        initializeRoles();
        roles.add(role);
    }

    public void addAllRoles(List<Role> roles) {
        initializeRoles();
        roles.addAll(roles);
    }

    private void initializeRoles() {
        if (roles == null)
            roles = new ArrayList<>();
    }

    private void initializeGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
    }
}