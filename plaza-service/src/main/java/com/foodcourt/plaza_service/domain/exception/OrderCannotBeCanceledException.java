package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.ORDER_CAN_NOT_BE_CANCELED_MESSAGE;

public class OrderCannotBeCanceledException extends RuntimeException {
    public OrderCannotBeCanceledException() {
        super(
                ORDER_CAN_NOT_BE_CANCELED_MESSAGE
        );
    }
}
