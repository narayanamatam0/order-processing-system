package com.ecommerce.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        @NotBlank @Size(max = 100) String customerId,
        @NotNull @Size(min = 1, max = 100) List<@NotNull @Valid Item> items,
        @NotNull @Pattern(regexp = "[A-Z]{3}", message = "must contain three uppercase letters")
        String currency
) {
    public record Item(
            @NotBlank @Size(max = 100) String productId,
            @NotNull @Positive Integer quantity,
            @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal unitPrice
    ) {
    }
}
