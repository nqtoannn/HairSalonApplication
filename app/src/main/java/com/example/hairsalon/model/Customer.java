package com.example.hairsalon.model;

public class Customer {
    private String name;
    private Integer age;
    private String id;
    private boolean gender;
    private String address;
    private String email;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Customer(String name, Integer age, String id, boolean gender, String address, String email, String phone){
        this.name = name;
        this.age = age;
        this.id = id;
        this.gender = gender;
        this.address =address;
        this.email = email;
        this.phone = phone;
    }
}
