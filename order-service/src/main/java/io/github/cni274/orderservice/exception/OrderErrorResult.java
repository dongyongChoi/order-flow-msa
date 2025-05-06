package io.github.cni274.orderservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorResult {
    TOO_MANY_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "너무 많은 주문 수량입니다."),
    ORDER_QUANTITY_IS_NEGATIVE(HttpStatus.BAD_REQUEST, "주문 수량은 0보다 작거나 같을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

}
