package com.ecommerce.order.dto;

import com.ecommerce.common.event.OrderItem;
import com.ecommerce.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        String customerId,
        List<OrderItem> items,
        BigDecimal totalAmount,
        String currency,
        OrderStatus status,
        Instant createdAt
) {
    public OrderResponse {
        items = List.copyOf(items);
    }
}
