package io.github.cni274.orderservice.service;

import io.github.cni274.orderservice.order.dto.OrderDto;
import io.github.cni274.orderservice.order.entity.Order;
import io.github.cni274.orderservice.order.exception.OrderErrorResult;
import io.github.cni274.orderservice.order.exception.OrderException;
import io.github.cni274.orderservice.order.repository.OrderRepository;
import io.github.cni274.orderservice.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 0})
    @DisplayName("주문 등록 실패 - 주문 수량은 0보다 작거나 같을 수 없음")
    void failedCreateOrder_IncorrectQuantity(int quantityParam) {
        OrderDto orderDto = OrderDto.builder()
                .userId(-1L)
                .productName("햄버거")
                .quantity(quantityParam)
                .build();

        OrderException orderException = assertThrows(OrderException.class, () -> orderService.createOrder(orderDto));

        assertThat(orderException.getErrorResult()).isEqualTo(OrderErrorResult.ORDER_QUANTITY_IS_NEGATIVE);
    }

    @Test
    @DisplayName("주문 등록 실패 - 주문 수량이 너무 많음")
    void failedCreateOrder_TooManyQuantity() {
        OrderDto orderDto = OrderDto.builder()
                .userId(-1L)
                .productName("햄버거")
                .quantity(10000)
                .build();

        OrderException orderException = assertThrows(OrderException.class, () -> orderService.createOrder(orderDto));
        assertThat(orderException.getErrorResult()).isEqualTo(OrderErrorResult.TOO_MANY_ORDER_QUANTITY);
    }

    @Test
    @DisplayName("주문 등록 성공")
    void successfulCreateOrder() {
        OrderDto orderDto = OrderDto.builder()
                .userId(-1L)
                .productName("햄버거")
                .quantity(10)
                .build();

        doReturn(Order.builder().build()).when(orderRepository).save(any(Order.class));

        orderService.createOrder(orderDto);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 조회 실패 - 주문이 존재하지 않음")
    void getOrder_OrderNotFound() {
        Long orderId = -1L;

        doReturn(Optional.empty())
                .when(orderRepository).findById(orderId);



        OrderException orderException = assertThrows(OrderException.class, () -> orderService.getOrder(orderId));

        assertThat(orderException.getErrorResult()).isEqualTo(OrderErrorResult.ORDER_NOT_FOUND);

        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("주문 조회 성공")
    void successfulGetOrder() {
        Long orderId = -1L;

        doReturn(Optional.of(Order.builder().build())).when(orderRepository).findById(orderId);

        OrderDto order = orderService.getOrder(orderId);

        assertThat(order).isNotNull();

        verify(orderRepository).findById(orderId);
    }

}