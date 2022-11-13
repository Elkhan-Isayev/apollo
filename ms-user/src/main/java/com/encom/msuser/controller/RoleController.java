package com.encom.msuser.controller;

import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.service.RoleService;
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
@RequestMapping(path = "/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<PrivilegeDto>> getAllRoles(@RequestParam(value="page", defaultValue="1") int page,
                                                          @RequestParam(value="size", defaultValue="10") int size) {
        if (page <= 0 || size <= 0) {
            throw new BadRequestException(String.format("controller.getAllRoles page = %s size = %s", page, size));
        }
        return roleService.getAllRoles(page, size);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAllRolesCount() {
        return roleService.getAllRolesCount();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeDto> getRoleById(@PathVariable String id) {
        if (id.isEmpty()) {
            throw new BadRequestException(String.format("controller.getRoleById"));
        }
        return roleService.getRoleById(id);
    }

    @PostMapping
    public ResponseEntity<PrivilegeDto> createNewRole(@RequestBody PrivilegeDto privilegeDto) {
        if (privilegeDto == null ||
                privilegeDto.getName().isEmpty() ||
                privilegeDto.getDescription().isEmpty()) {
            throw new BadRequestException(String.format("controller.createNewRole body = %s", privilegeDto));
        }
        return roleService.createNewRole(privilegeDto);
    }

    @PutMapping
    public ResponseEntity<PrivilegeDto> updateRole(@RequestBody PrivilegeDto privilegeDto) {
        if (privilegeDto == null ||
                privilegeDto.getId().isEmpty()) {
            throw new BadRequestException(String.format("controller.updateRole body = %s", privilegeDto));
        }
        return roleService.updateRole(privilegeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PrivilegeDto> deleteRole(@PathVariable String id) {
        if (id.isEmpty()) {
            throw new BadRequestException(String.format("controller.deleteRole"));
        }
        return roleService.deleteRole(id);
    }
}
