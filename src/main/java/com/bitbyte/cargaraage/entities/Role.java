package com.bitbyte.cargaraage.entities;

import com.bitbyte.cargaraage.models.Metadata;
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
@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    private Metadata metadata;
    @DBRef(db = "car_garaage")
    private List<User> users;
    @DBRef(db = "car_garaage")
    private List<Group> groups;

    public void addGroup(Group group) {
        initializeGroups();
        groups.add(group);
    }

    public void addGroups(List<Group> groups) {
        initializeGroups();
        this.groups.addAll(groups);
    }

    public void addUser(User user) {
        initializeUsers();
        users.add(user);
    }

    public void addUsers(List<User> users) {
        initializeUsers();
        this.users.addAll(users);
    }

    private void initializeGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
    }

    private void initializeUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
    }
}
