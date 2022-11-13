package com.encom.msuser.repository;

import com.encom.msuser.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String> {
//    List<Group> findGroupsByGroupUsers
}
