package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.ORDER_IS_NOT_IN_PREPARATION_MESSAGE;

public class OrderIsNotInPreparationException extends RuntimeException {
    public OrderIsNotInPreparationException() {
        super(
                ORDER_IS_NOT_IN_PREPARATION_MESSAGE
        );
    }
}
