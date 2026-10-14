package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        String customerId,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        String currency,
        OrderStatus status,
        Instant createdAt
) {
    public OrderResponse {
        items = List.copyOf(items);
    }
}
