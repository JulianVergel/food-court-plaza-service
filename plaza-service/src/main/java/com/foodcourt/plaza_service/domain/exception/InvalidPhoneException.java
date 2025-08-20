package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.PHONE_FIELD_INVALID_MESSAGE;

public class InvalidPhoneException extends RuntimeException {
    public InvalidPhoneException() {
        super(
                PHONE_FIELD_INVALID_MESSAGE
        );
    }
}
