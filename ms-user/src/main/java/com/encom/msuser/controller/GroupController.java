package com.encom.msuser.controller;

import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/groups")
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups(@RequestParam(value="page", defaultValue="1") int page,
                                                       @RequestParam(value="size", defaultValue="10") int size) {
        if (page <= 0 || size <= 0) {
            throw new BadRequestException(String.format("controller.getAllGroups page = %s size = %s", page, size));
        }
        return groupService.getAllGroups(page, size);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAllGroupsCount() {
        return groupService.getAllGroupsCount();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable String id) {
        if (id.isEmpty()) {
            throw new BadRequestException(String.format("controller.getGroupById"));
        }
        return groupService.getGroupById(id);
    }

    @PostMapping
    public ResponseEntity<GroupDto> createNewGroup(@RequestBody GroupDto groupDto) {
        if (groupDto == null ||
                groupDto.getName().isEmpty() ||
                groupDto.getDescription().isEmpty()) {
            throw new BadRequestException(String.format("controller.createNewGroup body = %s", groupDto));
        }
        return groupService.createNewGroup(groupDto);
    }

    @PutMapping
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto groupDto) {
        if (groupDto == null ||
                groupDto.getId().isEmpty()) {
            throw new BadRequestException(String.format("controller.updateGroup body = %s", groupDto));
        }
        return groupService.updateGroup(groupDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupDto> deleteGroup(@PathVariable String id) {
        if (id.isEmpty()) {
            throw new BadRequestException(String.format("controller.deleteGroup"));
        }
        return groupService.deleteGroup(id);
    }
}
