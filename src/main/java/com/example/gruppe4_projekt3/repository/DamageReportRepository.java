package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DamageReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public DamageReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class DamageReportRowMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            int carId = rs.getInt("car_id");
            double price = rs.getDouble("price");
            int employeeId = rs.getInt("employee_id");
            String customerId = rs.getString("customer_id");

            Car car = new Car();
            car.setCarId(carId);

            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);

            Customer customer = new Customer();
            customer.setId(customerId);

            return new DamageReport(car, price, employee, customer);
        }
    }

    public void save(DamageReport damageReport) {
        String sql = "INSERT INTO damage_report (car_id, price, employee_id, customer_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, damageReport.getCarId(), damageReport.getPrice(),
                damageReport.getEmployee() != null ? damageReport.getEmployee().getEmployeeId() : null,
                damageReport.getCustomer() != null ? damageReport.getCustomer().getId() : null);
    }

    public DamageReport findByCarId(int carId) {
        String sql = "SELECT * FROM damage_report WHERE car_id = ?";
        return jdbcTemplate.queryForObject(sql, new DamageReportRowMapper(), carId);
    }

    public List<DamageReport> findAll() {
        String sql = "SELECT * FROM damage_report";
        return jdbcTemplate.query(sql, new DamageReportRowMapper());
    }
}
