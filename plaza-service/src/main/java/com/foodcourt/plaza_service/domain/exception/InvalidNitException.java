package com.foodcourt.plaza_service.domain.exception;


import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.NIT_FIELD_MUST_BE_NUMERIC_MESSAGE;

public class InvalidNitException extends RuntimeException {
    public InvalidNitException() {
        super(
                NIT_FIELD_MUST_BE_NUMERIC_MESSAGE
        );
    }
}
