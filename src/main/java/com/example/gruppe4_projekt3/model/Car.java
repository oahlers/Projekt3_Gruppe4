package com.example.gruppe4_projekt3.model;

public class Car {
    private Integer carId;
    private Integer carEmission;
    private Integer year;
    private String brand;
    private String model;
    private String color;
    private String equipmentLevel; //udstyrniveau
    private String returnAddress;
    //private String fuelType;
    private String vehicleNumber; // registrerings nummer
    private String chassisNumber; // stelnummer
    private double price;
    private double registrationFee;

    private boolean isCarAvailable;
    private boolean readyForLoan;

    private int paymentTime;
    private int transportTime;

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
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

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
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

    public boolean isCarAvailable() {
        return isCarAvailable;
    }

    public void setCarAvailable(boolean carAvailable) {
        isCarAvailable = carAvailable;
    }

    public boolean isReadyForLoan() {
        return readyForLoan;
    }

    public void setReadyForLoan(boolean readyForLoan) {
        this.readyForLoan = readyForLoan;
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
