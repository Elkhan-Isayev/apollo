package com.encom.msuser.mapper;

import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class RoleMapper {
    public static final RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    public abstract PrivilegeDto mapToRoleDto(Privilege privilege);

    public abstract List<PrivilegeDto> mapToRoleDtoList(List<Privilege> privileges);

    public abstract Privilege mapToRole(PrivilegeDto PrivilegeDto);
}
