package com.encom.msuser.mapper;

import com.encom.msuser.model.dto.UserDto;
import com.encom.msuser.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class UserMapper {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserDto mapToUserDto(User user);

    public abstract List<UserDto> mapToUserDtoList(List<User> users);

    public abstract User mapToUser(UserDto userDto);
}
