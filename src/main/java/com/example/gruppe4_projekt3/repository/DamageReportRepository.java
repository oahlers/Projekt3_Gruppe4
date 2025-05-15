package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.Rental;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
        String sql = "INSERT INTO damage_report (car_id, mileage, employee_id, customer_email, report, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Integer employeeId = damageReport.getEmployee() != null ? damageReport.getEmployee().getEmployeeId() : null;

        for (int i = 0; i < damageReport.getReports().length; i++) {
            String reportDesc = damageReport.getReports()[i];
            BigDecimal price = BigDecimal.valueOf(damageReport.getPrices()[i]);

            if (reportDesc != null && !reportDesc.isEmpty()) {
                jdbcTemplate.update(sql,
                        damageReport.getCar().getCarId(),
                        damageReport.getMileage(),
                        employeeId,
                        damageReport.getCustomerEmail(),
                        reportDesc,
                        price);
            }
        }

        // Reset car status after damage reports inserted
        carRepository.resetAfterDamageReport(damageReport.getCar().getCarId());
    }


    public List<DamageReport> findAll() {
        String sql = "SELECT dr.*, c.brand, c.model, e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id";
        return jdbcTemplate.query(sql, new DamageReportRowMapper());
    }

    public Rental findLatestRentalByCarId(Long carId) {
        String sql = "SELECT * FROM rental WHERE car_id = ? ORDER BY start_date DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new RentalRowMapper(), carId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class DamageReportRowMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long carId = rs.getLong("car_id");
            String customerEmail = rs.getString("customer_email");
            int mileage = rs.getInt("mileage");

            Car car = new Car();
            car.setCarId(carId);
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));

            Employee employee = new Employee();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullName(rs.getString("employee_fullname"));

            return new DamageReport(car, employee, customerEmail, mileage);
        }
    }

    private static class RentalRowMapper implements RowMapper<Rental> {
        @Override
        public Rental mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rental rental = new Rental();
            rental.setRentalId(rs.getLong("rental_id"));
            rental.setCarId(rs.getLong("car_id"));
            rental.setCustomerName(rs.getString("customer_name"));
            rental.setCustomerEmail(rs.getString("customer_email"));
            rental.setDeliveryAddress(rs.getString("delivery_address"));
            return rental;
        }
    }
}