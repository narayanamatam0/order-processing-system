package com.ecommerce.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id", nullable = false)
    private UUID orderItemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    protected OrderItemEntity() {
        // Required by JPA.
    }

    public OrderItemEntity(
            Integer lineNumber,
            String productId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity) {
        this.lineNumber = lineNumber;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public OrderEntity getOrder() {
        return order;
    }

    void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
