package com.example.gruppe4_projekt3.model;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Customer;
import com.example.gruppe4_projekt3.model.Employee;

public class DamageReport {
    private int carId;
    private double price;
    private Car car;
    private Employee employee;
    private Customer customer;

    public DamageReport(Car car, double price, Employee employee, Customer customer) {
        this.car = car;
        this.carId = car.getCarId();
        this.price = price;
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
