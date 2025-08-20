package com.foodcourt.plaza_service.infrastructure.output.feign.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseDto {
    private Long id;
    private String name;
    private String description;
}
