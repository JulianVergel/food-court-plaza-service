package com.foodcourt.plaza_service.infrastructure.output.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessagingRequestDto {
    private String phone;
    private String message;
}
