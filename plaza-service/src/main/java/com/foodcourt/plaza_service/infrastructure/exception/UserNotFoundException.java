package com.foodcourt.plaza_service.infrastructure.exception;

import static com.foodcourt.plaza_service.infrastructure.utils.constants.FeignConstants.USER_NOT_FOUND_MESSAGE;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(
                USER_NOT_FOUND_MESSAGE
        );
    }
}
