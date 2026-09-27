package com.ecommerce.common.event;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** An order-time price snapshot; monetary values use decimal arithmetic. */
public record   OrderItem(
        @NotBlank String productId,
        @Positive int quantity,
        @NotNull @DecimalMin("0.00") BigDecimal unitPrice
) {
}
