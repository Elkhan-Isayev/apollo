package com.encom.msuser.mapper;

import com.encom.msuser.model.dto.RoleDto;
import com.encom.msuser.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class RoleMapper {
    public static final RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    public abstract RoleDto mapToRoleDto(Role role);

    public abstract List<RoleDto> mapToRoleDtoList(List<Role> Roles);

    public abstract Role mapToRole(RoleDto RoleDto);
}
