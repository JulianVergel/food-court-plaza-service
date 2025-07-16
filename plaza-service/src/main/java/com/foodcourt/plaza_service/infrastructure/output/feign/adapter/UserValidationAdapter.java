package com.foodcourt.plaza_service.infrastructure.output.feign.adapter;

import com.foodcourt.plaza_service.domain.spi.IUserValidationPort;
import com.foodcourt.plaza_service.infrastructure.output.feign.client.IUserFeignClient;
import com.foodcourt.plaza_service.infrastructure.output.feign.dto.response.UserResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.foodcourt.plaza_service.infrastructure.utils.constants.FeignConstants.*;

@RequiredArgsConstructor
public class UserValidationAdapter implements IUserValidationPort {

    private static final Logger logger = LoggerFactory.getLogger(UserValidationAdapter.class);
    private final IUserFeignClient userFeignClient;

    @Override
    public boolean isUserOwner(Long userId) {
        try {
            UserResponseDto user = userFeignClient.getUserById(userId);
            return user != null && user.getRole() != null && OWNER_ROLE_NAME.equals(user.getRole().getName());
        } catch (FeignException.NotFound e) {
            logger.error(USER_NOT_FOUND_MESSAGE, userId);
            return false;
        } catch (Exception e) {
            logger.error(FEIGN_CLIENT_ERROR_MESSAGE, userId, e);
            return false;
        }
    }
}
