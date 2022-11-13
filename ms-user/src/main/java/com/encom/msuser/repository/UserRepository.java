package com.encom.msuser.repository;

import com.encom.msuser.model.entity.Group;
import com.encom.msuser.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
}