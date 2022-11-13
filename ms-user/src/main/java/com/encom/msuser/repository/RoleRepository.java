package com.encom.msuser.repository;

import com.encom.msuser.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Privilege, String> {
}
