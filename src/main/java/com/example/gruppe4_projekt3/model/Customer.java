package com.example.gruppe4_projekt3.model;

public class Customer {
    private Long id;
    private String name;
    private String deliveryAddress;
    private int rentalMonths;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getRentalMonths() {
        return rentalMonths;
    }

    public void setRentalMonths(int rentalMonths) {
        this.rentalMonths = rentalMonths;
    }
}