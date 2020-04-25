package com.bitbyte.cargaraage.entities;

import com.bitbyte.cargaraage.models.Metadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private Metadata metadata;
    @DBRef
    private List<User> users;

    public void addUser(User user) {
        initializeUsers();
        users.add(user);
    }

    private void initializeUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    public void addAllUsers(List<User> users) {
        initializeUsers();
        this.users.addAll(users);
    }
}
