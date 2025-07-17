package com.foodcourt.plaza_service.infrastructure.exceptionhandler;

import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

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
}
