package com.encom.msuser.controller;

import com.encom.msuser.model.dto.UserDto;
import com.encom.msuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value="page", defaultValue="1") int page,
                                                     @RequestParam(value="size", defaultValue="10") int size) {

        return null;
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAllUsersCount() {
        return userService.getAllUsersCount();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int eventCategoryID) {

        return null;
    }

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {

        return null;
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {

        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable String id) {

        return null;
    }
}
