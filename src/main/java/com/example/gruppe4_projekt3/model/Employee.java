package com.example.gruppe4_projekt3.model;

// Model klasse for Employee
// [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
public class  Employee {
    private int employeeId;
    private String fullName;
    private String username;
    private String password;
    private String role;

    public Employee() {
    }

    public Employee(int employeeId, String fullName, String username, String password, String role) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = "";
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}