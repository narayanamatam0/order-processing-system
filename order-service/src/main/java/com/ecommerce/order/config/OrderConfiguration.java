package com.ecommerce.order.config;

import com.ecommerce.order.service.OrderIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfiguration {

    @Bean
    public OrderIdGenerator orderIdGenerator() {
        System.out.println("OrderIdGenerator bean created");
        return new OrderIdGenerator();
    }
}
