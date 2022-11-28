package com.encom.msuser.controller;

import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/groups")
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getAllGroups(@RequestParam(value="page", defaultValue="1") int page,
                                                       @RequestParam(value="size", defaultValue="10") int size) {
        if (page <= 0 || size <= 0) {
            throw new BadRequestException(String.format("controller.getAllGroups page = %s size = %s", page, size));
        }
        return groupService.getAllGroups(page, size);
    }

    @GetMapping("/count")
    public Long getAllGroupsCount() {
        return groupService.getAllGroupsCount();
    }

    @GetMapping("/{id}")
    public GroupDto getGroupById(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        return groupService.getGroupById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public GroupDto createNewGroup(@RequestBody GroupDto groupDto) {
        return groupService.createNewGroup(groupDto);
    }

    @PutMapping
    public GroupDto updateGroup(@RequestBody GroupDto groupDto) {
        return groupService.updateGroup(groupDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        groupService.deleteGroup(id);
    }

    //  Group privileges
    @GetMapping("/{groupId}/privileges")
    public List<PrivilegeDto> getGroupPrivileges(@PathVariable @NotEmpty(message = "id can not be empty") String groupId) {
        return groupService.getGroupPrivileges(groupId);
    }

    @PostMapping("/{groupId}/privileges/{privilegeId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addGroupPrivilege(@PathVariable @NotEmpty(message = "id can not be empty") String groupId,
                                  @PathVariable @NotEmpty(message = "id can not be empty") String privilegeId) {
        groupService.addGroupPrivilege(groupId, privilegeId);
    }

    @DeleteMapping("/{groupId}/privileges/{privilegeId}")
    public void deleteGroupPrivelege(@PathVariable @NotEmpty(message = "id can not be empty") String groupId,
                                     @PathVariable @NotEmpty(message = "id can not be empty") String privilegeId) {
        groupService.deleteGroupPrivilege(groupId, privilegeId);
    }
}
