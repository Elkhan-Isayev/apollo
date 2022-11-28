package com.encom.msuser.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class GroupDto {
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
