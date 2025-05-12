package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Gemmer en ny medarbejder i databasen.
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (fullname, username, password, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getFullName(), employee.getUsername(), employee.getPassword(), employee.getRole());
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
    // Viser en liste af samtlige medarbejdere.
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
    }

    public class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullName(rs.getString("fullname"));
            employee.setUsername(rs.getString("username"));
            employee.setPassword(rs.getString("password"));
            employee.setRole(rs.getString("role"));
            return employee;
        }
    }

}