package com.bitbyte.cargaraage.controllers;

import com.bitbyte.cargaraage.entities.Role;
import com.bitbyte.cargaraage.exceptionhandlers.RoleModificationException;
import com.bitbyte.cargaraage.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class RolesController {

    private final RoleService roleService;

    public RolesController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("car_garaage/v1/roles")
    public ResponseEntity<List<Role>> createRoles(@RequestBody List<Role> roles, @RequestHeader("Authorization") String bearerToken) {
        return new ResponseEntity<>(roleService.createRoles(roles, bearerToken), HttpStatus.CREATED);
    }

    @PutMapping("car_garaage/v1/roles/{roleId}")
    public ResponseEntity<?> updateRoleId(@PathVariable String roleId, String userId, String groupId,
                                          @RequestHeader("Authorization") String bearerToken) {

        if ((userId != null || !userId.isEmpty()) && (groupId != null || !groupId.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please pass either userId or groupId");
        } else if (userId != null && !userId.isEmpty()) {
            return new ResponseEntity<>(roleService.addUserToRole(userId, roleId, bearerToken), HttpStatus.OK);
        } else if (groupId != null && !groupId.isEmpty()) {
            return new ResponseEntity<>(roleService.addGroupToRole(groupId, roleId, bearerToken), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please pass either userId or groupId");
        }
    }
}
