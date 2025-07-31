package com.foodcourt.plaza_service.infrastructure.output.feign.adapter;

import com.foodcourt.plaza_service.domain.spi.IUserPersistencePort;
import com.foodcourt.plaza_service.infrastructure.exception.UserNotFoundException;
import com.foodcourt.plaza_service.infrastructure.output.feign.client.IUserFeignClient;
import com.foodcourt.plaza_service.infrastructure.output.feign.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.foodcourt.plaza_service.infrastructure.utils.constants.FeignConstants.USER_NOT_FOUND_MESSAGE;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserFeignClient userFeignClient;

    @Override
    public String findUserPhoneById(Long userId) {
        UserResponseDto user = userFeignClient.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user.getPhone();
    }
}
