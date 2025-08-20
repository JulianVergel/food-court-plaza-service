package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.USER_CAN_NOT_CANCEL_ORDER_MESSAGE;

public class UserCanNotCancelOrderException extends RuntimeException {
    public UserCanNotCancelOrderException() {
        super(
                USER_CAN_NOT_CANCEL_ORDER_MESSAGE
        );
    }
}
