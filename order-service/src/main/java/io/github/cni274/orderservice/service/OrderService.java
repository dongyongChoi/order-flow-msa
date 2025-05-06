package io.github.cni274.orderservice.service;

import io.github.cni274.orderservice.dto.OrderDto;
import io.github.cni274.orderservice.entity.Order;
import io.github.cni274.orderservice.exception.OrderErrorResult;
import io.github.cni274.orderservice.exception.OrderException;
import io.github.cni274.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private static final int MAX_QUANTITY = 100;

    @Transactional
    public void createOrder(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalArgumentException("orderDto cannot be null");
        }

        if (orderDto.getQuantity() <= 0) {
            throw new OrderException(OrderErrorResult.ORDER_QUANTITY_IS_NEGATIVE);
        }

        if (orderDto.getQuantity() > MAX_QUANTITY) {
            throw new OrderException(OrderErrorResult.TOO_MANY_ORDER_QUANTITY);
        }

        orderRepository.save(Order.valueOf(orderDto));
    }

    public OrderDto getOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            throw new OrderException(OrderErrorResult.ORDER_NOT_FOUND);
        }

        return OrderDto.valueOf(orderOptional.get());
    }
}
