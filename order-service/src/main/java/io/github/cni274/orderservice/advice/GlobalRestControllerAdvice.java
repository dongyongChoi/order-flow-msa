package io.github.cni274.orderservice.advice;

import io.github.cni274.orderservice.exception.UserException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        HttpStatus status = e.getErrorResult().getStatus();
        String code = e.getErrorResult().name();
        String message = e.getErrorResult().getMessage();

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(code, message));

    }

    @Getter
    @RequiredArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
