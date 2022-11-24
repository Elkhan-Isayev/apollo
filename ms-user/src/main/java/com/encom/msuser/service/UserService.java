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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @LogExecutionTime
    public ResponseEntity<List<UserDto>> getAllUsers(int page, int size) {
        List<User> users = new ArrayList<>();

        userRepository.findAll(PageRequest.of(page - 1, size)).forEach(users::add);
        if (users.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllUsers page = %s and size = %s", page, size));
        }

        List<UserDto> userDtoList = userMapper.mapToUserDtoList(users);

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<Long> getAllUsersCount() {
        long count = userRepository.count();

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<UserDto> getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException(String.format("service.getUserById id = %s", id));
        }

        UserDto userDto = userMapper.mapToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<UserDto> createNewUser(UserDto userDto) {
        User user = userMapper.mapToUser(userDto);

        User createdUser = userRepository.save(user);

        UserDto createdUserDto = userMapper.mapToUserDto(createdUser);

        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @LogExecutionTime
    public ResponseEntity<UserDto> updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElse(null);
        if (user == null) {
            throw new NotFoundException(String.format("service.updateUser id = %s", userDto.getId()));
        }

        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        User changedUser = userRepository.save(user);

        UserDto changedUserDto = userMapper.mapToUserDto(changedUser);

        return new ResponseEntity<>(changedUserDto, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteUser id = %s", id));
        }

        userRepository.deleteById(id);

        return new ResponseEntity(null, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<List<GroupDto>> getUserGroups(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.getUserGroups id = %s", id));
        }

        List<Group> groups = new ArrayList<>();
        userRepository
                .findById(id)
                .orElse(null)
                .getGroups()
                .stream()
                .forEach(group -> groups.add(group));

        if (groups.isEmpty()) {
            throw new NotFoundException(String.format("service.getUserGroups id = %s, userGroups is empty", id));
        }

        List<GroupDto> groupDtoList = groupMapper.mapToGroupDtoList(groups);

        return new ResponseEntity(groupDtoList, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity addUserGroup(String userId, GroupDto groupDto) {
        User user = userRepository.findById(userId).orElse(null);
        Group group = groupRepository.findById(groupDto.getId()).orElse(null);
        if (user == null || group == null) {
            throw new BadRequestException(String.format("service.addUserGroup userId = %s, groupId = %s", userId, groupDto.getId()));
        }

        List<Group> userGroups = user.getGroups();
        if (userGroups.contains(group)) {
            throw new BadRequestException(String.format("service.addUserGroup groupId = %s already exist for userId = %s", group.getId(), userId));
        }

        userGroups.add(group);
        user.setGroups(userGroups);
        userRepository.save(user);

        return new ResponseEntity(null, HttpStatus.CREATED);
    }
}
