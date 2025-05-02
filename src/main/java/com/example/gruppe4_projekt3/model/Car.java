package com.example.gruppe4_projekt3.model;

import java.time.LocalDate;

public class Car {
    private Long carId;
    private String image;
    private Integer carEmission;
    private Integer year;
    private String brand;
    private String model;
    private String color;
    private String equipmentLevel;
    private String returnAddress;
    private String vehicleNumber;
    private String chassisNumber;
    private double price;
    private double registrationFee;

    private boolean isAvailableForLoan;
    private boolean isReadyForUse;

    private int paymentTime;
    private int transportTime;

    private LocalDate rentalStartDate;
    private LocalDate readyForUseDate;

    // Getters and setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCarEmission() {
        return carEmission;
    }

    public void setCarEmission(Integer carEmission) {
        this.carEmission = carEmission;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEquipmentLevel() {
        return equipmentLevel;
    }

    public void setEquipmentLevel(String equipmentLevel) {
        this.equipmentLevel = equipmentLevel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public boolean isAvailableForLoan() {
        return isAvailableForLoan;
    }

    public void setAvailableForLoan(boolean availableForLoan) {
        isAvailableForLoan = availableForLoan;
    }

    public boolean isReadyForUse() {
        return isReadyForUse;
    }

    public void setReadyForUse(boolean readyForUse) {
        isReadyForUse = readyForUse;
    }

    public int getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(int paymentTime) {
        this.paymentTime = paymentTime;
    }

    public int getTransportTime() {
        return transportTime;
    }

    public void setTransportTime(int transportTime) {
        this.transportTime = transportTime;
    }

    public LocalDate getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDate rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDate getReadyForUseDate() {
        return readyForUseDate;
    }

    public void setReadyForUseDate(LocalDate readyForUseDate) {
        this.readyForUseDate = readyForUseDate;
    }
}
