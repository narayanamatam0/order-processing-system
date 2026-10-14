package com.ecommerce.order.service;

import java.util.UUID;

public class OrderIdGenerator {
    public UUID generate() {
        return UUID.randomUUID();
    }
}
