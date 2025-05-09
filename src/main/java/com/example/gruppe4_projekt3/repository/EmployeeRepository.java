package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Gemmer en ny medarbejder i databasen.
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (employee_id, fullname, username, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getEmployeeId(), employee.getFullName(), employee.getUsername(), employee.getPassword());
    }


    // Viser en liste af samtlige medarbejdere.
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
    }
}