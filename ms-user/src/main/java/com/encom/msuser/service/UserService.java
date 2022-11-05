package com.encom.msuser.service;

import com.encom.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;

//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public ResponseEntity<Long> getAllUsersCount() {
        long count = userRepository.count();

        return new ResponseEntity<Long>(count, HttpStatus.OK);

    }
}
