package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.RESTAURANT_NOT_FOUND_MESSAGE;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super(
                RESTAURANT_NOT_FOUND_MESSAGE
        );
    }
}
