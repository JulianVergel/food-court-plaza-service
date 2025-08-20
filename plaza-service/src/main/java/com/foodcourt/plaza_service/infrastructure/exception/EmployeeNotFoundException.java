package com.foodcourt.plaza_service.infrastructure.exception;

import static com.foodcourt.plaza_service.infrastructure.utils.constants.FeignConstants.EMPLOYEE_NOT_FOUND_MESSAGE;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super(
                EMPLOYEE_NOT_FOUND_MESSAGE
        );
    }
}
