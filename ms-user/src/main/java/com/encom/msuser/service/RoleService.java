package com.encom.msuser.service;

import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.RoleMapper;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.model.entity.Privilege;
import com.encom.msuser.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper = RoleMapper.INSTANCE;

    public ResponseEntity<List<PrivilegeDto>> getAllRoles(int page, int size) {
        List<Privilege> privileges = new ArrayList<>();

        roleRepository.findAll(PageRequest.of(page - 1, size)).forEach(privileges::add);
        if (privileges.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllRoles page = %s and size = %s", page, size));
        }

        List<PrivilegeDto> privilegeDtoList = roleMapper.mapToRoleDtoList(privileges);

        return new ResponseEntity<>(privilegeDtoList, HttpStatus.OK);
    }

    public ResponseEntity<Long> getAllRolesCount() {
        long count = roleRepository.count();

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    public ResponseEntity<PrivilegeDto> getRoleById(String id) {
        Privilege privilege = roleRepository.findById(id).orElse(null);
        if (privilege == null) {
            throw new NotFoundException(String.format("service.getRoleById id = %s", id));
        }

        PrivilegeDto privilegeDto = roleMapper.mapToRoleDto(privilege);

        return new ResponseEntity<>(privilegeDto, HttpStatus.OK);
    }

    public ResponseEntity<PrivilegeDto> createNewRole(PrivilegeDto privilegeDto) {
        Privilege privilege = roleMapper.mapToRole(privilegeDto);

        Privilege createdPrivilege = roleRepository.save(privilege);

        PrivilegeDto createdPrivilegeDto = roleMapper.mapToRoleDto(createdPrivilege);

        return new ResponseEntity<>(createdPrivilegeDto, HttpStatus.CREATED);
    }

    public ResponseEntity<PrivilegeDto> updateRole(PrivilegeDto privilegeDto) {
        Privilege privilege = roleRepository.findById(privilegeDto.getId()).orElse(null);
        if (privilege == null) {
            throw new NotFoundException(String.format("service.updateRole id = %s", privilegeDto.getId()));
        }

        privilege.setName(privilegeDto.getName());
        privilege.setDescription(privilegeDto.getDescription());

        Privilege changedPrivilege = roleRepository.save(privilege);

        PrivilegeDto changedPrivilegeDto = roleMapper.mapToRoleDto(changedPrivilege);

        return new ResponseEntity<>(changedPrivilegeDto, HttpStatus.OK);
    }

    public ResponseEntity deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteRole id = %s", id));
        }

        roleRepository.deleteById(id);

        return new ResponseEntity(null, HttpStatus.OK);
    }
}
