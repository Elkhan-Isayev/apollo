package com.encom.msuser.controller;

import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.model.dto.UserDto;
import com.encom.msuser.service.UserService;;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value="page", defaultValue="1") int page,
                                                     @RequestParam(value="size", defaultValue="10") int size) {
        if (page <= 0 || size <= 0) {
            throw new BadRequestException(String.format("controller.getAllUsers page = %s size = %s", page, size));
        }
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAllUsersCount() {
        return userService.getAllUsersCount();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        if (id.isEmpty()) {
            throw new BadRequestException(String.format("controller.getUserById"));
        }
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {
        if (userDto == null ||
                userDto.getUsername().isEmpty() ||
                userDto.getName().isEmpty() ||
                userDto.getSurname().isEmpty() ||
                userDto.getEmail().isEmpty()) {
            throw new BadRequestException(String.format("controller.createNewUser body = %s", userDto));
        }
        return userService.createNewUser(userDto);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        if (userDto == null ||
                userDto.getId().isEmpty()) {
            throw new BadRequestException(String.format("controller.updateUser body = %s", userDto));
        }
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable String id) {
        if (id.isEmpty()) {
            throw new BadRequestException(String.format("controller.deleteUser"));
        }
        return userService.deleteUser(id);
    }
}
