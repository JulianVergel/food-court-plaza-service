package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.USER_NOT_AN_OWNER_MESSAGE;

public class UserNotAnOwnerException extends RuntimeException {
    public UserNotAnOwnerException() {
        super(USER_NOT_AN_OWNER_MESSAGE);
    }
}
