package io.github.cni274.orderservice.order.repository;

import io.github.cni274.orderservice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
