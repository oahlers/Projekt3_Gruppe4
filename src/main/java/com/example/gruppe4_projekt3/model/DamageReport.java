package com.example.gruppe4_projekt3.model;

public class DamageReport {
    private int carId;
    private double price;
    private Car car;
    private Employee employee;
    private Customer customer;

    public DamageReport(int carId, double price, Car car, Employee employee, Customer customer) {
        this.carId = carId;
        this.price = price;
        this.car = car;
        this.employee = employee;
        this.customer = customer;
    }

    public int getCarId() {
        return carId;
    }

    public double getPrice() {
        return price;
    }

    public Car getCar() {
        return car;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
