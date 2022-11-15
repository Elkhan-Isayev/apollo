package com.encom.msuser.repository;

import com.encom.msuser.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, String> {
}
