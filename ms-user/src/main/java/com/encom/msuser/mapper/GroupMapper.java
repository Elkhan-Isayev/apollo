package com.encom.msuser.mapper;

import com.encom.msuser.model.dto.GroupDto;
import com.encom.msuser.model.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class GroupMapper {
    public static final GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    public abstract GroupDto mapToGroupDto(Group group);

    public abstract List<GroupDto> mapToGroupDtoList(List<Group> groups);

    public abstract Group mapToGroup(GroupDto groupDto);
}
