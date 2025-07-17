package com.foodcourt.plaza_service.infrastructure.output.feign.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private RoleResponseDto role;
}
