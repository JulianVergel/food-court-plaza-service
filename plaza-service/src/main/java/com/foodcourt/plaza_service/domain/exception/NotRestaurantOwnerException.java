package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.NOT_RESTAURANT_OWNER_MESSAGE;

public class NotRestaurantOwnerException extends RuntimeException {
    public NotRestaurantOwnerException() {
        super(
                NOT_RESTAURANT_OWNER_MESSAGE
        );
    }
}
