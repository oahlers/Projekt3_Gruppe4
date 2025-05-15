package com.example.gruppe4_projekt3.model;

import java.util.List;

public class DamageReport {
    private Long reportId;
    private Car car;
    private Employee employee;
    private String customerEmail;
    private String[] reports = new String[10];
    private double[] prices = new double[10];
    private int mileage;

    public void setDamages(List<String> descriptions, List<Double> prices) {
        this.reports = descriptions.toArray(new String[0]);
        this.prices = prices.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public DamageReport() {
    }

    public DamageReport(Car car, Employee employee, String customerEmail, int mileage) {
        this.car = car;
        this.employee = employee;
        this.customerEmail = customerEmail;
        this.mileage = mileage;
    }


    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
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
