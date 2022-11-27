package com.encom.msuser.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class UserDto {
    private String id;
    @NotEmpty(message = "Username can not be empty")
    private String username;
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @NotEmpty(message = "Surname can not be empty")
    private String surname;
    @Email(message = "Email is not valid")
    private String email;
}
