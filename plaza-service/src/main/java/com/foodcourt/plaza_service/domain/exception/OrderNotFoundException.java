package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.ORDER_NOT_FOUND_MESSAGE;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super(
                ORDER_NOT_FOUND_MESSAGE
        );
    }
}
