package com.example.hairsalon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrderItem {
    private String orderItemId;
    private Double price;
    private Integer quantity;
    private String productItemId;
    private String productItemUrl;
    private String productItemName;

    public OrderItem(String orderItemId, Double price, Integer quantity, String productItemId, String productItemUrl, String productItemName) {
        this.orderItemId = orderItemId;
        this.price = price;
        this.quantity = quantity;
        this.productItemId = productItemId;
        this.productItemUrl = productItemUrl;
        this.productItemName = productItemName;
    }

    public OrderItem() {
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(String productItemId) {
        this.productItemId = productItemId;
    }

    public String getProductItemUrl() {
        return productItemUrl;
    }

    public void setProductItemUrl(String productItemUrl) {
        this.productItemUrl = productItemUrl;
    }

    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
    }
}
