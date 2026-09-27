package com.ecommerce.common.event;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Payment outcome for the order service. paymentId identifies the attempt,
 * including a failed attempt. failureReason is null on success.
 */
public record PaymentProcessedEvent(
        @NotNull UUID eventId,
        @NotNull UUID orderId,
        @NotNull UUID paymentId,
        @NotNull PaymentStatus status,
        @NotNull @DecimalMin("0.00") BigDecimal amount,
        @NotNull @Pattern(regexp = "[A-Z]{3}") String currency,
        String failureReason,
        @NotNull Instant occurredAt
) {
}
