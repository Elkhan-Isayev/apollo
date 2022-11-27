package com.encom.msuser.service;

import com.encom.msuser.configuration.annotation.LogExecutionTime;
import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.GroupMapper;
import com.encom.msuser.mapper.UserMapper;
import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.model.dto.UserDto;
import com.encom.msuser.model.entity.Group;
import com.encom.msuser.model.entity.User;
import com.encom.msuser.repository.GroupRepository;
import com.encom.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @LogExecutionTime
    public List<UserDto> getAllUsers(int page, int size) {
        List<User> users = new ArrayList<>();

        userRepository.findAll(PageRequest.of(page - 1, size))
                .forEach(users::add);

        if (users.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllUsers page = %s and size = %s", page, size));
        }

        return userMapper.mapToUserDtoList(users);
    }

    @LogExecutionTime
    public Long getAllUsersCount() {
        var count = userRepository.count();
        return count;
    }

    @LogExecutionTime
    public UserDto getUserById(String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("service.getUserById id = %s", id)));
        return userMapper.mapToUserDto(user);
    }

    @LogExecutionTime
    public UserDto createNewUser(UserDto userDto) {
        var user = userMapper.mapToUser(userDto);

        var createdUser = userRepository.save(user);

        return userMapper.mapToUserDto(createdUser);
    }

    @LogExecutionTime
    public UserDto updateUser(UserDto userDto) {
        var user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("service.updateUser id = %s", userDto.getId())));

        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        var changedUser = userRepository.save(user);

        return userMapper.mapToUserDto(changedUser);
    }

    @LogExecutionTime
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteUser id = %s", id));
        }
        userRepository.deleteById(id);
    }

    @LogExecutionTime
    public List<GroupDto> getUserGroups(String id) {
        List<Group> groups = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("service.getUserGroups id = %s user does not exist", id)))
                .getGroups();

        if (CollectionUtils.isEmpty(groups)) {
            throw new NotFoundException(String.format("service.getUserGroups id = %s, userGroups is empty", id));
        }

        return groupMapper.mapToGroupDtoList(groups);
    }

    @LogExecutionTime
    public void addUserGroup(String userId, String groupId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(String.format("service.addUserGroup userId = %s", userId)));
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(String.format("service.addUserGroup groupId = %s", groupId)));

        user.getGroups()
                .stream()
                .filter(perGroup -> group == perGroup)
                .findFirst()
                .ifPresentOrElse(
                        (presentGroup) -> {
                            throw new BadRequestException(String.format("service.addUserGroup groupId = %s already exist for userId = %s", presentGroup.getId(), userId));
                        },
                        () -> {
                            user.getGroups().add(group);
                            userRepository.save(user);
                        }
                );
    }

    @LogExecutionTime
    public void deleteUserGroup(String userId, String groupId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(String.format("service.deleteUserGroup userId = %s", userId)));
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(String.format("service.deleteUserGroup groupId = %s", groupId)));

        user.getGroups()
                .stream()
                .filter(perGroup -> perGroup == group)
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("service.deleteUserGroup groupId = %s does not exist for userId = %s", groupId, userId)));

        user.getGroups().remove(group);
        userRepository.save(user);
    }
}
