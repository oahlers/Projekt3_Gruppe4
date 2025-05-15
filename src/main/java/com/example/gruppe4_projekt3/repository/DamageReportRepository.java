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
        carRepository.resetAfterDamageReport(damageReport.getCar().getCarId());
    }

    public List<DamageReport> findAll() {
        String sql = "SELECT " +
                "dr.*, " +
                "c.car_id AS car_car_id, " +
                "c.brand AS car_brand, " +
                "c.model AS car_model, " +
                "c.chassis_number AS car_chassis_number, " +
                "c.license_plate AS car_license_plate, " +
                "e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id";

        return jdbcTemplate.query(sql, new DamageReportRowMapper(true));
    }

    public DamageReport findLatestByCarId(Long carId) {
        String sql = "SELECT " +
                "dr.*, " +
                "c.car_id AS car_car_id, " +
                "c.brand AS car_brand, " +
                "c.model AS car_model, " +
                "c.chassis_number AS car_chassis_number, " +
                "c.license_plate AS car_license_plate, " +
                "e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id " +
                "WHERE dr.car_id = ? " +
                "ORDER BY dr.report_id DESC " +
                "LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new DamageReportRowMapper(true), carId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
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

        private final boolean includeCarDetails;

        public DamageReportRowMapper(boolean includeCarDetails) {
            this.includeCarDetails = includeCarDetails;
        }

        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long carId = rs.getLong("car_id");
            String customerEmail = rs.getString("customer_email");
            int mileage = rs.getInt("mileage");

            Car car = new Car();
            car.setCarId(carId);

            if (includeCarDetails) {
                car.setLicensePlate(rs.getString("car_license_plate"));
                car.setChassisNumber(rs.getString("car_chassis_number"));
                car.setBrand(rs.getString("car_brand"));
                car.setModel(rs.getString("car_model"));
            }

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

            rental.setRentalMonths(rs.getInt("rental_months"));
            rental.setMileage(rs.getInt("mileage"));

            try {
                rental.setSubscriptionType(rs.getString("subscription_type_id"));
            } catch (SQLException e) {
                rental.setSubscriptionType(null);
            }

            return rental;
        }
    }

}