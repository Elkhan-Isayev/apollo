package com.encom.msuser.service;

import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.RoleMapper;
import com.encom.msuser.model.dto.RoleDto;
import com.encom.msuser.model.entity.Role;
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

    public ResponseEntity<List<RoleDto>> getAllRoles(int page, int size) {
        List<Role> roles = new ArrayList<>();

        roleRepository.findAll(PageRequest.of(page - 1, size)).forEach(roles::add);
        if (roles.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllRoles page = %s and size = %s", page, size));
        }

        List<RoleDto> roleDtoList = roleMapper.mapToRoleDtoList(roles);

        return new ResponseEntity<>(roleDtoList, HttpStatus.OK);
    }

    public ResponseEntity<Long> getAllRolesCount() {
        long count = roleRepository.count();

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    public ResponseEntity<RoleDto> getRoleById(String id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            throw new NotFoundException(String.format("service.getRoleById id = %s", id));
        }

        RoleDto roleDto = roleMapper.mapToRoleDto(role);

        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    public ResponseEntity<RoleDto> createNewRole(RoleDto roleDto) {
        Role role = roleMapper.mapToRole(roleDto);

        Role createdRole = roleRepository.save(role);

        RoleDto createdRoleDto = roleMapper.mapToRoleDto(createdRole);

        return new ResponseEntity<>(createdRoleDto, HttpStatus.CREATED);
    }

    public ResponseEntity<RoleDto> updateRole(RoleDto roleDto) {
        Role role = roleRepository.findById(roleDto.getId()).orElse(null);
        if (role == null) {
            throw new NotFoundException(String.format("service.updateRole id = %s", roleDto.getId()));
        }

        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        Role changedRole = roleRepository.save(role);

        RoleDto changedRoleDto = roleMapper.mapToRoleDto(changedRole);

        return new ResponseEntity<>(changedRoleDto, HttpStatus.OK);
    }

    public ResponseEntity deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteRole id = %s", id));
        }

        roleRepository.deleteById(id);

        return new ResponseEntity(null, HttpStatus.OK);
    }
}
