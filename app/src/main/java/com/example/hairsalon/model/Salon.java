package com.example.hairsalon.model;

public class Salon {
    private Integer id;
    private String salonName;
    private String address;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;

    public Salon(Integer id, String saloneName, String address, String phoneNumber){
        this.id = id;
        this.salonName = saloneName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
   public Integer getId(){return id;}
    public String getSalonName() {
        return salonName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
