package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.ORDER_CANNOT_BE_ASSIGNED_MESSAGE;

public class OrderCannotBeAssignedException extends RuntimeException {
    public OrderCannotBeAssignedException() {
        super(
                ORDER_CANNOT_BE_ASSIGNED_MESSAGE
        );
    }
}
