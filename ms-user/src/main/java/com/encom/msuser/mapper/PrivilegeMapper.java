package com.encom.msuser.mapper;

import com.encom.msuser.model.dto.PrivilegeDto;
import com.encom.msuser.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class PrivilegeMapper {
    public static final PrivilegeMapper INSTANCE = Mappers.getMapper(PrivilegeMapper.class);

    public abstract PrivilegeDto mapToPrivilegeDto(Privilege privilege);

    public abstract List<PrivilegeDto> mapToPrivilegeDtoList(List<Privilege> privileges);

    public abstract Privilege mapToPrivilege(PrivilegeDto PrivilegeDto);
}
