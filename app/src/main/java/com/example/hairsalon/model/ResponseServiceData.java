package com.example.hairsalon.model;

import java.util.List;

public class ResponseServiceData {
    private String status;
    private String message;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<HairService> hairService;

        public List<HairService> getHairService() {
            return hairService;
        }

        public void setHairService(List<HairService> hairService) {
            this.hairService = hairService;
        }
    }
}
