package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.DISH_NOT_FOUND_MESSAGE;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException() {
        super(
                DISH_NOT_FOUND_MESSAGE
        );
    }
}
