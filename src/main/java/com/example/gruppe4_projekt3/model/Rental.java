package com.example.gruppe4_projekt3.model;

import java.time.LocalDate;

public class Rental {
    private Long rentalId;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerName;
    private String customerEmail;
    private String deliveryAddress;
    private int rentalMonths;
    private LocalDate readyForUseDate;
    private Integer paymentTime;
    private Integer transportTime;
    private int subscriptionTypeId;
    private int mileage;
    private boolean isPurchased;

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public LocalDate getReadyForUseDate() {
        return readyForUseDate;
    }

    public void setReadyForUseDate(LocalDate readyForUseDate) {
        this.readyForUseDate = readyForUseDate;
    }

    public Integer getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Integer paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getTransportTime() {
        return transportTime;
    }

    public void setTransportTime(Integer transportTime) {
        this.transportTime = transportTime;
    }

    public int getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(int subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }
}