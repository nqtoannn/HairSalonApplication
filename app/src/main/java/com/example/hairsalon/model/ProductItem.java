package com.example.hairsalon.model;

public class ProductItem {
    private Integer id;
    private String productItemName;
    private Double price;
    private Integer quantityInStock;
    private Integer warrantyTime;
    private String status;
    private String imageUrl;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductItem(Integer id, String productItemName, Double price, Integer quantityInStock, Integer warrantyTime, String status, String imageUrl, String description) {
        this.id = id;
        this.productItemName = productItemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.warrantyTime = warrantyTime;
        this.status = status;
        this.imageUrl = imageUrl;
        this.description = description;
    }


    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(Integer warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
