package com.encom.msuser.repository;

import com.encom.msuser.model.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, String> {
}
