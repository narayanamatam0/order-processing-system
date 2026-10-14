package com.ecommerce.order.service;

import com.ecommerce.common.event.OrderItem;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.model.OrderStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final OrderIdGenerator orderIdGenerator;
    public OrderService(OrderIdGenerator orderIdGenerator) {
        this.orderIdGenerator = orderIdGenerator;
        System.out.println("OrderService constructor executed");
    }
    // Learning step 1: process-local storage. Restarting the service clears orders.
    private final Map<UUID, OrderResponse> orders = new ConcurrentHashMap<>();

    public OrderResponse createOrder(CreateOrderRequest request) {
        List<OrderItem> items = request.items().stream()
                .map(item -> new OrderItem(item.productId(), item.quantity(), item.unitPrice()))
                .toList();
        BigDecimal totalAmount = items.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderResponse order = new OrderResponse(
                orderIdGenerator.generate(), request.customerId(), items, totalAmount,
                request.currency(), OrderStatus.PENDING, Instant.now());
        orders.put(order.orderId(), order);
        return order;
    }

    public OrderResponse getOrder(UUID orderId) {
        OrderResponse order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }
}
