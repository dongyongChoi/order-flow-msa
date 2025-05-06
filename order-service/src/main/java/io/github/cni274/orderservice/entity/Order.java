package io.github.cni274.orderservice.entity;

import io.github.cni274.orderservice.dto.OrderDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column
    private String productName;

    @Column
    private int quantity;

    public static Order valueOf(OrderDto orderDto) {
        return Order.builder()
                .userId(orderDto.getUserId())
                .productName(orderDto.getProductName())
                .quantity(orderDto.getQuantity())
                .build();
    }
}
