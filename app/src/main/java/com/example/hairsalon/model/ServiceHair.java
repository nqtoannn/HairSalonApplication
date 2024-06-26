package com.example.hairsalon.model;

public class ServiceHair {
    private Integer id;
    private String serviceName;
    private String description;
    private String url;
    private double price;

    public ServiceHair(Integer id, String serviceName, String description, String url, double price) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.url = url;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
