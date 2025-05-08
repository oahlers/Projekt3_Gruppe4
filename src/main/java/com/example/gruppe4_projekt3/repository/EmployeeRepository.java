package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Gemmer en ny medarbejder i databasen.
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (employee_id, fullname, username, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getEmployeeId(), employee.getFullName(), employee.getUsername(), employee.getPassword());
    }

    // Finder en medarbejder ud fra deres ID.
    public Employee findByEmployeeId(int employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), employeeId);
        } catch (Exception e) {
            return null;
        }
    }

    // Finder en medarbejder ud fra deres brugernavn.
    public Employee findByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), username);
        } catch (Exception e) {
            return null;
        }
    }

    // Finder en medarbejder ud fra både employee ID og brugernavn.
    public Employee findByEmployeeIdAndUsername(int employeeId, String username) {
        String sql = "SELECT * FROM employees WHERE employee_id = ? AND username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), employeeId, username);
        } catch (Exception e) {
            return null;
        }
    }
}