package io.github.cni274.orderservice.order.controller;

import io.github.cni274.orderservice.order.dto.OrderDto;
import io.github.cni274.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto) {
        orderService.createOrder(orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        OrderDto order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }
}
