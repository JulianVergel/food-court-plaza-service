package com.foodcourt.plaza_service.domain.exception;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.CATEGORY_NOT_FOUND_MESSAGE;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super(
                CATEGORY_NOT_FOUND_MESSAGE
        );
    }
}
