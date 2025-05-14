package com.example.gruppe4_projekt3.model;

public class DamageReport {
    private Car car;
    private Employee employee;
    private String customerEmail;
    private String[] reports = new String[10];
    private double[] prices = new double[10];
    private int mileage;

    public DamageReport(Car car, Employee employee, String customerEmail, int mileage) {
        this.car = car;
        this.employee = employee;
        this.customerEmail = customerEmail;
        this.mileage = mileage;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
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

    public String[] getReports() {
        return reports;
    }

    public void setReports(String[] reports) {
        this.reports = reports;
    }

    public double[] getPrices() {
        return prices;
    }

    public void setPrices(double[] prices) {
        this.prices = prices;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
}
