package com.example.hairsalon.model;

public class CartItem {
    private Integer id;
    private String productItemName;
    private Integer quantity;
    private String imageUrl;
    private Double price;

    public CartItem(Integer id, String productItemName, Integer quantity, String imageUrl, Double price) {
        this.id = id;
        this.productItemName = productItemName;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
