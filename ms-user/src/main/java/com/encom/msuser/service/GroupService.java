package com.encom.msuser.service;

import com.encom.msuser.configuration.annotation.LogExecutionTime;
import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.GroupMapper;
import com.encom.msuser.mapper.PrivilegeMapper;
import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.model.entity.Group;
import com.encom.msuser.model.entity.Privilege;
import com.encom.msuser.repository.GroupRepository;
import com.encom.msuser.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final PrivilegeRepository privilegeRepository;

    private final GroupMapper groupMapper = GroupMapper.INSTANCE;
    private final PrivilegeMapper privilegeMapper = PrivilegeMapper.INSTANCE;

    @LogExecutionTime
    public List<GroupDto> getAllGroups(int page, int size) {
        List<Group> groups = new ArrayList<>();
        groupRepository.findAll(PageRequest.of(page - 1, size)).forEach(groups::add);
        if (groups.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllGroups page = %s and size = %s", page, size));
        }
        return groupMapper.mapToGroupDtoList(groups);
    }

    @LogExecutionTime
    public Long getAllGroupsCount() {
        long count = groupRepository.count();
        return count;
    }

    @LogExecutionTime
    public GroupDto getGroupById(String id) {
        var group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("service.getGroupById id = %s", id)));
        return groupMapper.mapToGroupDto(group);
    }

    @LogExecutionTime
    public GroupDto createNewGroup(GroupDto groupDto) {
        var group = groupMapper.mapToGroup(groupDto);
        var createdGroup = groupRepository.save(group);
        return groupMapper.mapToGroupDto(createdGroup);
    }

    @LogExecutionTime
    public GroupDto updateGroup(GroupDto groupDto) {
        var group = groupRepository.findById(groupDto.getId()).orElseThrow(() -> new NotFoundException(String.format("service.updateGroup id = %s", groupDto.getId())));
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());
        Group changedGroup = groupRepository.save(group);
        return groupMapper.mapToGroupDto(changedGroup);
    }

    @LogExecutionTime
    public void deleteGroup(String id) {
        if (!groupRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteGroup id = %s", id));
        }
        groupRepository.deleteById(id);
    }

    @LogExecutionTime
    public List<PrivilegeDto> getGroupPrivileges(String groupId) {
        List<Privilege> privileges = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(String.format("service.getGroupPrivileges id = %s group does not exist", groupId)))
                .getPrivileges();
        if(CollectionUtils.isEmpty(privileges)) {
            throw new NotFoundException(String.format("service.getGroupPrivileges id = %s, groupPrivileges is empty", groupId));
        }
        return privilegeMapper.mapToPrivilegeDtoList(privileges);
    }

    @LogExecutionTime
    public void addGroupPrivilege(String groupId, String privilegeId) {
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(String.format("service.addGroupPrivilege groupId = %s", groupId)));
        var privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new BadRequestException(String.format("service.addGroupPrivilege privilegeId = %s", privilegeId)));
        group.getPrivileges()
                .stream()
                .filter(perPrivilege -> privilege == perPrivilege)
                .findFirst()
                .ifPresentOrElse(
                        (presentPrivilege) -> {
                            throw new BadRequestException(String.format("service.addGroupPrivilege privilegeId = %s already exist for groupId = %s", presentPrivilege.getId(), groupId));
                        },
                        () -> {
                            group.getPrivileges().add(privilege);
                            groupRepository.save(group);
                        }
                );
    }

    @LogExecutionTime
    public void deleteGroupPrivilege(String groupId, String privilegeId) {
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(String.format("service.deleteGroupPrivilege groupId = %s", groupId)));
        var privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new BadRequestException(String.format("service.deleteGroupPrivilege privilegeId = %s", privilegeId)));
        group.getPrivileges()
                .stream()
                .filter(perPrivilege -> perPrivilege == privilege)
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("service.deleteGroupPrivilege privilegeId = %s does not exist for groupId = %s", privilegeId, groupId)));
        group.getPrivileges().remove(privilege);
        groupRepository.save(group);
    }
}
