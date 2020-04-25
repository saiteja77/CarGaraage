package com.bitbyte.cargaraage.services;

import com.bitbyte.cargaraage.entities.Group;
import com.bitbyte.cargaraage.entities.User;
import com.bitbyte.cargaraage.exceptionhandlers.GroupModificationException;
import com.bitbyte.cargaraage.repositories.GroupsRepository;
import com.bitbyte.cargaraage.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bitbyte.cargaraage.utilities.SecurityUtils.getUserName;
import static com.bitbyte.cargaraage.utilities.SecurityUtils.removeBearerString;

@Service
@Slf4j
public class GroupService {

    private final GroupsRepository groupsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public GroupService(GroupsRepository groupsRepository, UsersRepository usersRepository) {
        this.groupsRepository = groupsRepository;
        this.usersRepository = usersRepository;
    }

    public List<Group> createGroups(List<Group> groups, String bearerToken) {
        bearerToken = removeBearerString(bearerToken);
        User user = usersRepository.findByUserName(getUserName(bearerToken)).get();
        groups.forEach(group -> {
            group.getMetadata().setCreatedBy(user);
            group.getMetadata().setLastUpdateBy(user);
        });
        return groupsRepository.saveAll(groups);
    }

    public Group addUserToGroup(String userId, String groupId, String bearerToken) throws GroupModificationException {
        log.info("GroupService :: addUserToGroup: Fetching the user -> {}", userId);

        Optional<User> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            log.info("GroupService :: addUserToGroup: User -> {} found", userId);
            log.info("GroupService :: addUserToGroup: Fetching the group -> {}", groupId);
            Optional<Group> group = groupsRepository.findById(groupId);
            if (group.isPresent() && group.get().getMetadata().isRequestable()) {
                log.info("GroupService :: addUserToGroup: Group -> {} found", userId);
                setLastUpdatedBy(group.get(), bearerToken);

                group.get().addUser(user.get());
                Group savedGroup = groupsRepository.save(group.get());
                log.info("GroupService :: addUserToGroup: Added the user -> {} to the group -> {}", userId, groupId);
                user.get().addGroup(group.get());
                usersRepository.save(user.get());
                log.info("GroupService :: addUserToGroup: Added the group -> {} to the user -> {}", groupId, userId);
                return savedGroup;
            } else throw new GroupModificationException("Group not found");
        } else throw new GroupModificationException("User not found");
    }

    public Group addUsersToGroup(List<String> userIds, String groupId, String bearerToken) {
        Optional<Group> group = groupsRepository.findById(groupId);
        if (group.isPresent() && group.get().getMetadata().isRequestable()) {
            Group g = group.get();
            List<User> users = usersRepository.findAllByIdIn(userIds);
            if (!userIds.isEmpty()) {
                g.addAllUsers(users);
                setLastUpdatedBy(group.get(), bearerToken);
                Group savedGroup = groupsRepository.save(g);
                for (User user : users) {
                    user.addGroup(g);
                }
                usersRepository.saveAll(users);
                return savedGroup;
            } else throw new GroupModificationException("No User found");
        } else throw new GroupModificationException("Group not found");
    }

    private void setLastUpdatedBy(Group group, String bearerToken) {
        User requestedUser = usersRepository.findByUserName(getUserName(removeBearerString(bearerToken))).get();
        group.getMetadata().setLastUpdateBy(requestedUser);
    }
}
