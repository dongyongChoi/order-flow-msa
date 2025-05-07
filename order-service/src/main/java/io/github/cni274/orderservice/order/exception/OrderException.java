package io.github.cni274.orderservice.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderException extends RuntimeException {
    private final OrderErrorResult errorResult;
}
