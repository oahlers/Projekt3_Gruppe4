package com.example.gruppe4_projekt3.model;

public class  DamageReport {
    private Car car;
    private double price;
    private Employee employee;
    private String customerEmail;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    private String report;

    public DamageReport(Car car, double price, Employee employee, String customerEmail, String report) {
        this.car = car;
        this.price = price;
        this.employee = employee;
        this.customerEmail = customerEmail;
        this.report = report;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }


}
