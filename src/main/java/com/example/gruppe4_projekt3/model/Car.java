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

    public Car() {
        this.carId = carId;
        this.carEmission = carEmission;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.equipmentLevel = equipmentLevel;
        this.returnAddress = returnAddress;
        this.vehicleNumber = vehicleNumber;
        this.chassisNumber = chassisNumber;
        this.price = price;
        this.registrationFee = registrationFee;
        this.isCarAvailable = isCarAvailable;
    }

    public Integer getCarId() {
        return carId;
    }

    public Integer getCarEmission() {
        return carEmission;
    }

    public Integer getYear() {
        return year;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getEquipmentLevel() {
        return equipmentLevel;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public double getPrice() {
        return price;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public boolean isCarAvailable() {
        return isCarAvailable;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public void setCarEmission(Integer carEmission) {
        this.carEmission = carEmission;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setEquipmentLevel(String equipmentLevel) {
        this.equipmentLevel = equipmentLevel;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public void setCarAvailable(boolean carAvailable) {
        isCarAvailable = carAvailable;
    }
}