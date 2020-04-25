package com.bitbyte.cargaraage.services;

import com.bitbyte.cargaraage.entities.Group;
import com.bitbyte.cargaraage.entities.Role;
import com.bitbyte.cargaraage.entities.User;
import com.bitbyte.cargaraage.exceptionhandlers.RoleModificationException;
import com.bitbyte.cargaraage.repositories.GroupsRepository;
import com.bitbyte.cargaraage.repositories.RolesRepository;
import com.bitbyte.cargaraage.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bitbyte.cargaraage.utilities.SecurityUtils.getUserName;
import static com.bitbyte.cargaraage.utilities.SecurityUtils.removeBearerString;

@Service
@Slf4j
public class RoleService {

    private final RolesRepository rolesRepository;
    private final GroupsRepository groupsRepository;
    private final UsersRepository usersRepository;
    private final GroupService groupService;

    @Autowired
    public RoleService(RolesRepository rolesRepository, GroupsRepository groupsRepository, UsersRepository usersRepository, GroupService groupService) {
        this.rolesRepository = rolesRepository;
        this.groupsRepository = groupsRepository;
        this.usersRepository = usersRepository;
        this.groupService = groupService;
    }

    public Role addGroupToRole(String groupId, String roleId, String bearerToken) throws RoleModificationException{
        Optional<Role> role = rolesRepository.findById(roleId);
        if (role.isPresent() && role.get().getMetadata().isRequestable()) {
            Optional<Group> group = groupsRepository.findById(groupId);
            if (group.isPresent()) {
                Role r = role.get();
                setLastUpdateBy(r, bearerToken);
                role.get().addGroup(group.get());
                Role savedRole = rolesRepository.save(role.get());
                log.info("RoleService :: addGroup: Successfully added the group -> {} to the role -> {}", groupId, roleId);

                if (r.getUsers() != null || !r.getUsers().isEmpty()) {
                    List<String> userIds = r.getUsers().stream().map(User::getId).collect(Collectors.toList());
                    groupService.addUsersToGroup(userIds, groupId, bearerToken);
                }
                return savedRole;
            } else throw new RoleModificationException("Group not found");
        } else throw new RoleModificationException("Role not found");
    }

    public Role addUserToRole(String userId, String roleId, String bearerToken) throws RoleModificationException {
        Optional<Role> role = rolesRepository.findById(roleId);
        if (role.isPresent() && role.get().getMetadata().isRequestable()) {
            Optional<User> user = usersRepository.findById(userId);
            if (user.isPresent()) {
                setLastUpdateBy(role.get(), bearerToken);
                role.get().addUser(user.get());
                Role savedRole = rolesRepository.save(role.get());
                user.get().addRole(savedRole);
                usersRepository.save(user.get());
                return savedRole;
            } else throw new RoleModificationException("User not found");
        } else throw new RoleModificationException("Role not found");
    }

    public List<Role> createRoles(List<Role> roles, String bearerToken) {
        bearerToken = removeBearerString(bearerToken);
        User user = usersRepository.findByUserName(getUserName(bearerToken)).get();
        roles.forEach(role -> {
            role.getMetadata().setCreatedBy(user);
            role.getMetadata().setLastUpdateBy(user);
        });
        return rolesRepository.saveAll(roles);
    }

    private void setLastUpdateBy(Role role, String bearerToken) {
        User requestedUser = usersRepository.findByUserName(getUserName(removeBearerString(bearerToken))).get();
        role.getMetadata().setLastUpdateBy(requestedUser);
    }
}
