package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DamageReportRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CarRepository carRepository;

    public DamageReportRepository(JdbcTemplate jdbcTemplate, CarRepository carRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.carRepository = carRepository;
    }

    public void save(DamageReport damageReport) {
        String sql = "INSERT INTO damage_report (car_id, price, employee_id, customer_email, report) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                damageReport.getCar().getCarId(),
                damageReport.getPrice(),
                damageReport.getEmployee() != null ? damageReport.getEmployee().getEmployeeId() : null,
                damageReport.getCustomerEmail(),
                damageReport.getReport());

        carRepository.resetAfterDamageReport(damageReport.getCar().getCarId());
    }

    public DamageReport findByCarId(int carId) {
        String sql = "SELECT dr.*, c.brand, c.model, e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id " +
                "WHERE dr.car_id = ?";
        return jdbcTemplate.queryForObject(sql, new DamageReportRowMapper(), carId);
    }

    public List<DamageReport> findAll() {
        String sql = "SELECT dr.*, c.brand, c.model, e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id";
        return jdbcTemplate.query(sql, new DamageReportRowMapper());
    }

    private static class DamageReportRowMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long carId = rs.getLong("car_id");
            String report = rs.getString("report");
            double price = rs.getDouble("price");
            int employeeId = rs.getInt("employee_id");
            String customerEmail = rs.getString("customer_email");

            Car car = new Car();
            car.setCarId(carId);
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));

            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);
            employee.setFullName(rs.getString("employee_fullname"));

            return new DamageReport(car, price, employee, customerEmail, report);
        }
    }
}
