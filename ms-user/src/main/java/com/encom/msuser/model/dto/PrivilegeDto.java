package com.encom.msuser.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PrivilegeDto {
    private String id;
    private String name;
    private String description;
}