package com.encom.msuser.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class PrivilegeDto {
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
