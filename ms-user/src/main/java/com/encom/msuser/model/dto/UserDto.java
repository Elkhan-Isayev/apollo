package com.encom.msuser.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;
}
