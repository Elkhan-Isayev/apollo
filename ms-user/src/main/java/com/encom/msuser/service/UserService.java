package com.encom.msuser.service;

import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.UserMapper;
import com.encom.msuser.model.dto.UserDto;
import com.encom.msuser.model.entity.User;
import com.encom.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public ResponseEntity<List<UserDto>> getAllUsers(int page, int size) {
        List<User> users = new ArrayList<>();

        userRepository.findAll(PageRequest.of(page - 1, size)).forEach(users::add);
        if (users.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllUsers page = %s and size = %s", page, size));
        }

        List<UserDto> userDtoList = userMapper.mapToUserDtoList(users);

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    public ResponseEntity<Long> getAllUsersCount() {
        long count = userRepository.count();

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    public ResponseEntity<UserDto> getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException(String.format("service.getUserById id = %s", id));
        }

        UserDto userDto = userMapper.mapToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public ResponseEntity<UserDto> createNewUser(UserDto userDto) {
        User user = userMapper.mapToUser(userDto);

        User createdUser = userRepository.save(user);

        UserDto createdUserDto = userMapper.mapToUserDto(createdUser);

        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

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

    public ResponseEntity deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteUser id = %s", id));
        }

        userRepository.deleteById(id);

        return new ResponseEntity(null, HttpStatus.OK);
    }
}
