package com.example.hairsalon.model;

public class HairService {
    private Integer id;
    private String serviceName;
    private Double price;
    private String description;
    private String url;

    public HairService(Integer id, String serviceName, Double price, String description, String url) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.description = description;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
