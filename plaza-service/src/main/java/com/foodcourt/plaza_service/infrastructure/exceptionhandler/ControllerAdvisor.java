package com.foodcourt.plaza_service.infrastructure.exceptionhandler;

import com.foodcourt.plaza_service.domain.exception.CategoryNotFoundException;
import com.foodcourt.plaza_service.domain.exception.NotRestaurantOwnerException;
import com.foodcourt.plaza_service.domain.exception.RestaurantNotFoundException;
import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.*;

@ControllerAdvice
public class ControllerAdvisor {
    private static final String MESSAGE = "message";

    @ExceptionHandler(UserNotAnOwnerException.class)
    public ResponseEntity<Map<String, String>> handleUserNotAnOwnerException(
            UserNotAnOwnerException userNotAnOwnerException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, userNotAnOwnerException.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNotFoundException(
            RestaurantNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, RESTAURANT_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(NotRestaurantOwnerException.class)
    public ResponseEntity<Map<String, String>> handleNotRestaurantOwnerException(
            NotRestaurantOwnerException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, NOT_RESTAURANT_OWNER_MESSAGE));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFoundException(
            CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, CATEGORY_NOT_FOUND_MESSAGE));
    }
}
