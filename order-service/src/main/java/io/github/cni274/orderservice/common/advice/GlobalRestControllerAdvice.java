package io.github.cni274.orderservice.common.advice;

import io.github.cni274.orderservice.order.exception.OrderErrorResult;
import io.github.cni274.orderservice.order.exception.OrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<?> handleOrderException(OrderException ex) {
        OrderErrorResult errorResult = ex.getErrorResult();
        HttpStatus status = errorResult.getStatus();
        String message = errorResult.getMessage();
        return ResponseEntity.status(status).body(message);
    }
}
