package com.encom.msuser.controller;

import com.encom.msuser.exception.BadRequestException;
import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.service.PrivilegeService;
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
@RequestMapping(path = "/privileges")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @GetMapping
    public List<PrivilegeDto> getAllPrivileges(@RequestParam(value="page", defaultValue="1") int page,
                                                          @RequestParam(value="size", defaultValue="10") int size) {
        if (page <= 0 || size <= 0) {
            throw new BadRequestException(String.format("controller.getAllPrivileges page = %s size = %s", page, size));
        }
        return privilegeService.getAllPrivileges(page, size);
    }

    @GetMapping("/count")
    public Long getAllPrivilegesCount() {
        return privilegeService.getAllPrivilegesCount();
    }

    @GetMapping("/{id}")
    public PrivilegeDto getPrivilegeById(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        return privilegeService.getPrivilegeById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PrivilegeDto createNewPrivilege(@RequestBody PrivilegeDto privilegeDto) {
        return privilegeService.createNewPrivilege(privilegeDto);
    }

    @PutMapping
    public PrivilegeDto updatePrivilege(@RequestBody PrivilegeDto privilegeDto) {
        return privilegeService.updatePrivilege(privilegeDto);
    }

    @DeleteMapping("/{id}")
    public void deletePrivilege(@PathVariable @NotEmpty(message = "id can not be empty") String id) {
        privilegeService.deletePrivilege(id);
    }
}
