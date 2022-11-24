package com.encom.msuser.service;

import com.encom.msuser.configuration.annotation.LogExecutionTime;
import com.encom.msuser.exception.NotFoundException;
import com.encom.msuser.mapper.GroupMapper;
import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.model.entity.Group;
import com.encom.msuser.repository.GroupRepository;
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
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @LogExecutionTime
    public ResponseEntity<List<GroupDto>> getAllGroups(int page, int size) {
        List<Group> groups = new ArrayList<>();

        groupRepository.findAll(PageRequest.of(page - 1, size)).forEach(groups::add);
        if (groups.isEmpty()) {
            throw new NotFoundException(String.format("service.getAllGroups page = %s and size = %s", page, size));
        }

        List<GroupDto> groupDtoList = groupMapper.mapToGroupDtoList(groups);

        return new ResponseEntity<>(groupDtoList, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<Long> getAllGroupsCount() {
        long count = groupRepository.count();

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<GroupDto> getGroupById(String id) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            throw new NotFoundException(String.format("service.getGroupById id = %s", id));
        }

        GroupDto groupDto = groupMapper.mapToGroupDto(group);

        return new ResponseEntity<>(groupDto, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity<GroupDto> createNewGroup(GroupDto groupDto) {
        Group group = groupMapper.mapToGroup(groupDto);

        Group createdGroup = groupRepository.save(group);

        GroupDto createdGroupDto = groupMapper.mapToGroupDto(createdGroup);

        return new ResponseEntity<>(createdGroupDto, HttpStatus.CREATED);
    }

    @LogExecutionTime
    public ResponseEntity<GroupDto> updateGroup(GroupDto groupDto) {
        Group group = groupRepository.findById(groupDto.getId()).orElse(null);
        if (group == null) {
            throw new NotFoundException(String.format("service.updateGroup id = %s", groupDto.getId()));
        }

        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());

        Group changedGroup = groupRepository.save(group);

        GroupDto changedGroupDto = groupMapper.mapToGroupDto(changedGroup);

        return new ResponseEntity<>(changedGroupDto, HttpStatus.OK);
    }

    @LogExecutionTime
    public ResponseEntity deleteGroup(String id) {
        if (!groupRepository.existsById(id)) {
            throw new NotFoundException(String.format("service.deleteGroup id = %s", id));
        }

        groupRepository.deleteById(id);

        return new ResponseEntity(null, HttpStatus.OK);
    }
}
