package com.encom.msuser.repository;

import com.encom.msuser.model.entity.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, String> {
}
