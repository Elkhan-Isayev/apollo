package com.encom.msuser.service;

import com.encom.msuser.configuration.annotation.LogExecutionTime;
import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.PrivilegeMapper;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.model.entity.Privilege;
import com.encom.msuser.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper = PrivilegeMapper.INSTANCE;

    @LogExecutionTime
    public ResponseEntity<List<PrivilegeDto>> getAllPrivileges(int page, int size) {
        List<Privilege> privileges = new ArrayList<>();

        privilegeRepository.findAll(PageRequest.of(page - 1, size)).forEach(privileges::add);
        if (privileges.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllPrivileges page = %s and size = %s", page, size));
        }

        List<PrivilegeDto> privilegeDtoList = privilegeMapper.mapToPrivilegeDtoList(privileges);

        return new ResponseEntity<>(privilegeDtoList, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<Long> getAllPrivilegesCount() {
        long count = privilegeRepository.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<PrivilegeDto> getPrivilegeById(String id) {
        Privilege privilege = privilegeRepository.findById(id).orElse(null);
        if (privilege == null) {
            throw new NotFoundException(String.format("service.getPrivilegeById id = %s", id));
        }

        PrivilegeDto privilegeDto = privilegeMapper.mapToPrivilegeDto(privilege);

        return new ResponseEntity<>(privilegeDto, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<PrivilegeDto> createNewPrivilege(PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeMapper.mapToPrivilege(privilegeDto);

        Privilege createdPrivilege = privilegeRepository.save(privilege);

        PrivilegeDto createdPrivilegeDto = privilegeMapper.mapToPrivilegeDto(createdPrivilege);

        return new ResponseEntity<>(createdPrivilegeDto, HttpStatus.CREATED);
    }

    @LogExecutionTime
    public ResponseEntity<PrivilegeDto> updatePrivilege(PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeRepository.findById(privilegeDto.getId()).orElse(null);
        if (privilege == null) {
            throw new NotFoundException(String.format("service.updatePrivilege id = %s", privilegeDto.getId()));
        }

        privilege.setName(privilegeDto.getName());
        privilege.setDescription(privilegeDto.getDescription());

        Privilege changedPrivilege = privilegeRepository.save(privilege);

        PrivilegeDto changedPrivilegeDto = privilegeMapper.mapToPrivilegeDto(changedPrivilege);

        return new ResponseEntity<>(changedPrivilegeDto, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity deletePrivilege(String id) {
        if (!privilegeRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deletePrivilege id = %s", id));
        }

        privilegeRepository.deleteById(id);

        return new ResponseEntity(null, HttpStatus.OK);
    }
}
