package com.ecommerce.common.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Emitted only after all items are reserved; carries the payment context.
 * eventId identifies this message; orderId correlates the order workflow.
 */
public record InventoryReservedEvent(
        @NotNull UUID eventId,
        @NotNull UUID orderId,
        @NotBlank String customerId,
        @NotEmpty List<@NotNull @Valid OrderItem> items,
        @NotNull @DecimalMin("0.00") BigDecimal totalAmount,
        @NotNull @Pattern(regexp = "[A-Z]{3}") String currency,
        @NotNull Instant occurredAt
) {
    public InventoryReservedEvent {
        items = List.copyOf(items);
    }
}
