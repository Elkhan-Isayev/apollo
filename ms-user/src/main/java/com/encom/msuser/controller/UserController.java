package com.encom.msuser.controller;

import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.model.dto.UserDto;
import com.encom.msuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(value="page", defaultValue="1") int page,
                                                     @RequestParam(value="size", defaultValue="10") int size) {
        if (page <= 0 || size <= 0) {
            throw new BadRequestException(String.format("controller.getAllUsers page = %s size = %s", page, size));
        }

        return userService.getAllUsers(page, size);
    }

    @GetMapping("/count")
    public Long getAllUsersCount() {
        return userService.getAllUsersCount();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody @Valid UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        if (userDto == null ||
                userDto.getId().isEmpty()) {
            throw new BadRequestException(String.format("controller.updateUser body = %s", userDto));
        }
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/groups")
    public List<GroupDto> getUserGroups(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        return userService.getUserGroups(id);
    }

    @PostMapping("/{userId}/groups/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addUserGroup(@PathVariable @NotEmpty(message = "id can not be empty") String userId,
                             @PathVariable @NotEmpty(message = "id can not be empty") String groupId) {
        userService.addUserGroup(userId, groupId);
    }

    @DeleteMapping("/{userId}/groups/{groupId}")
    public void deleteUserGroup(@PathVariable @NotEmpty(message = "id can not be empty") String userId,
                                @PathVariable @NotEmpty(message = "id can not be empty") String groupId) {
        userService.deleteUserGroup(userId, groupId);
    }
}
