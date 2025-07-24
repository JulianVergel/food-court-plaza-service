package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.INVALID_PIN_MESSAGE;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException() {
        super(
                INVALID_PIN_MESSAGE
        );
    }
}
