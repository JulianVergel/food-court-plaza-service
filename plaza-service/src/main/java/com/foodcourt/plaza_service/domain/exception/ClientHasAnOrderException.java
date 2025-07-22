package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.CLIENT_HAS_AN_ORDER_MESSAGE;

public class ClientHasAnOrderException extends RuntimeException {
    public ClientHasAnOrderException() {
        super(
                CLIENT_HAS_AN_ORDER_MESSAGE
        );
    }
}
