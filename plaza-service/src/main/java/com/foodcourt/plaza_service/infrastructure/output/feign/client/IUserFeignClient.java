package com.foodcourt.plaza_service.infrastructure.output.feign.client;

import com.foodcourt.plaza_service.infrastructure.output.feign.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.client.user-service.url}")
public interface IUserFeignClient {
    @GetMapping("/api/v1/user/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}
