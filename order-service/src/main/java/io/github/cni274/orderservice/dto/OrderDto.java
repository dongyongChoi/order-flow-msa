package io.github.cni274.orderservice.dto;

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
}
