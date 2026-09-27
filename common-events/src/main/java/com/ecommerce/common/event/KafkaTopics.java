package com.ecommerce.common.event;

/** Publish messages with orderId.toString() as the Kafka record key. */
public final class KafkaTopics {
    public static final String ORDER_CREATED = "order-created";
    public static final String INVENTORY_RESERVED = "inventory-reserved";
    public static final String PAYMENT_PROCESSED = "payment-processed";

    private KafkaTopics() {
    }
}
