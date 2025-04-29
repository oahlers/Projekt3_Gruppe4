package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Car;
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

    public DamageReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class DamageReportRowMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            int carId = rs.getInt("car_id");
            double price = rs.getDouble("price");
            int employeeId = rs.getInt("employee_id");
            String customerEmail = rs.getString("customer_email");

            Car car = new Car();
            car.setCarId(carId);

            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);

            return new DamageReport(car, price, employee, customerEmail);
        }
    }


    public void save(DamageReport damageReport) {
        String sql = "INSERT INTO damage_report (car_id, price, employee_id) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                damageReport.getCar().getCarId(),
                damageReport.getPrice(),
                damageReport.getEmployee() != null ? damageReport.getEmployee().getEmployeeId() : null);
    }

    public DamageReport findByCarId(int carId) {
        String sql = "SELECT * FROM damage_report WHERE car_id = ?";
        return jdbcTemplate.queryForObject(sql, new DamageReportRowMapper(), carId);
    }

    public List<DamageReport> findAll() {
        String sql = "SELECT * FROM damage_report";
        return jdbcTemplate.query(sql, new DamageReportRowMapper());
    }

    @Repository
    public class CarRepository {

        private final JdbcTemplate jdbcTemplate;

        public CarRepository(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public List<Car> findCarsWithDamageReports() {
            String sql = "SELECT DISTINCT c.* FROM car c " +
                    "JOIN damage_report dr ON c.car_id = dr.car_id";

            return jdbcTemplate.query(sql, new RowMapper<Car>() {
                @Override
                public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Car car = new Car();
                    car.setCarId(rs.getInt("car_id"));
                    car.setBrand(rs.getString("brand"));
                    car.setModel(rs.getString("model"));
                    car.setColor(rs.getString("color"));
                    car.setPrice(rs.getDouble("price"));
                    return car;
                }
            });
        }
    }

}
