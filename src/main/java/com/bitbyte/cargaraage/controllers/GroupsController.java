package com.bitbyte.cargaraage.controllers;

import com.bitbyte.cargaraage.entities.Group;
import com.bitbyte.cargaraage.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupsController {

    private final GroupService groupService;

    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/car_garaage/v1/groups")
    public ResponseEntity<List<Group>> createGroups(@RequestBody List<Group> groups, @RequestHeader("Authorization") String bearerToken) {

        return new ResponseEntity<>(groupService.createGroups(groups, bearerToken), HttpStatus.OK);
    }

    @PutMapping("/car_garaage/v1/groups/{groupId}")
    public ResponseEntity<Group> modifyGroup(@RequestParam String userId,
                                             @PathVariable String groupId,
                                             @RequestHeader("Authorization") String bearerToken) {
        return new ResponseEntity<>(groupService.addUserToGroup(userId, groupId, bearerToken), HttpStatus.OK);
    }
}