package com.encom.msuser.service;

import com.encom.msuser.configuration.annotation.LogExecutionTime;
import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.PrivilegeMapper;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.model.entity.Privilege;
import com.encom.msuser.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper = PrivilegeMapper.INSTANCE;

    @LogExecutionTime
    public List<PrivilegeDto> getAllPrivileges(int page, int size) {
        List<Privilege> privileges = new ArrayList<>();
        privilegeRepository.findAll(PageRequest.of(page - 1, size)).forEach(privileges::add);
        if (privileges.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllPrivileges page = %s and size = %s", page, size));
        }
        return privilegeMapper.mapToPrivilegeDtoList(privileges);
    }

    @LogExecutionTime
    public Long getAllPrivilegesCount() {
        long count = privilegeRepository.count();
        return count;
    }

    @LogExecutionTime
    public PrivilegeDto getPrivilegeById(String id) {
        var privilege = privilegeRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("service.getPrivilegeById id = %s", id)));
        return privilegeMapper.mapToPrivilegeDto(privilege);
    }

    @LogExecutionTime
    public PrivilegeDto createNewPrivilege(PrivilegeDto privilegeDto) {
        var privilege = privilegeMapper.mapToPrivilege(privilegeDto);
        var createdPrivilege = privilegeRepository.save(privilege);
        return privilegeMapper.mapToPrivilegeDto(createdPrivilege);
    }

    @LogExecutionTime
    public PrivilegeDto updatePrivilege(PrivilegeDto privilegeDto) {
        var privilege = privilegeRepository.findById(privilegeDto.getId()).orElseThrow(() -> new NotFoundException(String.format("service.updatePrivilege id = %s", privilegeDto.getId())));
        privilege.setName(privilegeDto.getName());
        privilege.setDescription(privilegeDto.getDescription());
        Privilege changedPrivilege = privilegeRepository.save(privilege);
        return privilegeMapper.mapToPrivilegeDto(changedPrivilege);
    }

    @LogExecutionTime
    public void deletePrivilege(String id) {
        if (!privilegeRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deletePrivilege id = %s", id));
        }
        privilegeRepository.deleteById(id);
    }
}
