package io.github.cni274.orderservice.order.dto;

import io.github.cni274.orderservice.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long userId;
    private String productName;
    private int quantity;

    public static OrderDto valueOf(Order order) {
        return OrderDto.builder()
                .userId(order.getUserId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .build();
    }
}
