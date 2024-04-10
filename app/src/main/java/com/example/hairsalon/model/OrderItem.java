package com.example.hairsalon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrderItem {
    private Integer orderItemId;
    private Double price;
    private Integer quantity;
    private Integer productItemId;
    private String productItemUrl;
    private String productItemName;

    public OrderItem(Integer orderItemId, Double price, Integer quantity, Integer productItemId, String productItemUrl, String productItemName) {
        this.orderItemId = orderItemId;
        this.price = price;
        this.quantity = quantity;
        this.productItemId = productItemId;
        this.productItemUrl = productItemUrl;
        this.productItemName = productItemName;
    }

    public OrderItem() {
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
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

    public Integer getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(Integer productItemId) {
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
