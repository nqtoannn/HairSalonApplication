package com.example.hairsalon.model;

public class Appointment {
    private String id;
    private String customerName;
    private String appointmentTime;
    private String appointmentDate;
    private String serviceName;
    private String status;
    private String userName;
    private String salonAddress;
    private String salonName;

    public Appointment(String id, String customerName, String appointmentTime, String appointmentDate, String serviceName, String status, String userName, String salonAddress, String salonName) {
        this.id = id;
        this.customerName = customerName;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
        this.serviceName = serviceName;
        this.status = status;
        this.userName = userName;
        this.salonAddress = salonAddress;
        this.salonName = salonName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSalonAddress() {
        return salonAddress;
    }

    public void setSalonAddress(String salonAddress) {
        this.salonAddress = salonAddress;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }
}
