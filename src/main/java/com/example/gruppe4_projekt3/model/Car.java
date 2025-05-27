package com.example.gruppe4_projekt3.model;

// Model klassen for Car
// // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
public class Car {
    private Long carId;
    private int carEmission;
    private int year;
    private String brand;
    private String model;
    private String color;
    private String equipmentLevel;
    private String vehicleNumber;
    private String chassisNumber;
    private String licensePlate;
    private double price;
    private double registrationFee;
    private String image;
    private boolean isRented;
    private boolean needsDamageReport;
    private int averageAvailabilityDays;
    private int averageRentalDuration;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public int getCarEmission() {
        return carEmission;
    }

    public void setCarEmission(int carEmission) {
        this.carEmission = carEmission;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
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

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public boolean isNeedsDamageReport() {
        return needsDamageReport;
    }

    public void setNeedsDamageReport(boolean needsDamageReport) {
        this.needsDamageReport = needsDamageReport;
    }

    public int getAverageAvailabilityDays() {
        return averageAvailabilityDays;
    }

    public void setAverageAvailabilityDays(int averageAvailabilityDays) {
        this.averageAvailabilityDays = averageAvailabilityDays;
    }

    public int getAverageRentalDuration() {
        return averageRentalDuration;
    }

    public void setAverageRentalDuration(int averageRentalDuration) {
        this.averageRentalDuration = averageRentalDuration;
    }
}