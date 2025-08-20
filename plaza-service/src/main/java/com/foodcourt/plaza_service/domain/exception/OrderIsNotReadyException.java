package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.ORDER_IS_NOT_READY_MESSAGE;

public class OrderIsNotReadyException extends RuntimeException {
    public OrderIsNotReadyException() {
        super(
                ORDER_IS_NOT_READY_MESSAGE
        );
    }
}
