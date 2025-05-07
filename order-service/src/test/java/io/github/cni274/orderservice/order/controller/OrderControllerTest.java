package io.github.cni274.orderservice.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cni274.orderservice.common.advice.GlobalRestControllerAdvice;
import io.github.cni274.orderservice.order.dto.OrderDto;
import io.github.cni274.orderservice.order.exception.OrderErrorResult;
import io.github.cni274.orderservice.order.exception.OrderException;
import io.github.cni274.orderservice.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setControllerAdvice(new GlobalRestControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("주문 등록 실패 - OrderException 발생")
    void failedCreateOrder_Required() throws Exception {
        OrderDto orderDto = OrderDto.builder()
                .userId(-1L)
                .productName("test")
                .quantity(1)
                .build();

        doThrow(new OrderException(OrderErrorResult.TOO_MANY_ORDER_QUANTITY))
                .when(orderService)
                .createOrder(any(OrderDto.class));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/orders")
                        .content(objectMapper.writeValueAsString(orderDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 등록 성공")
    void successCreateOrder() throws Exception {
        OrderDto orderDto = OrderDto.builder()
                .userId(-1L)
                .productName("test")
                .quantity(1)
                .build();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/orders")
                        .content(objectMapper.writeValueAsString(orderDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("주문 조회 실패 - id로 조회된 데이터 없음")
    void failedGetOrder_OrderNotFound() throws Exception {
        doThrow(new OrderException(OrderErrorResult.ORDER_NOT_FOUND))
                .when(orderService).getOrder(-1L);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/orders/-1")
        );

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문 조회 성공")
    void successGetOrder() throws Exception {
        doReturn(OrderDto.builder().build())
                .when(orderService)
                .getOrder(-1L);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/orders/-1")
        );

        resultActions.andExpect(status().isOk());
    }
}