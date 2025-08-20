package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE;

public class InvalidRestaurantNameException extends RuntimeException {
    public InvalidRestaurantNameException() {
        super(
                NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE
        );
    }
}
