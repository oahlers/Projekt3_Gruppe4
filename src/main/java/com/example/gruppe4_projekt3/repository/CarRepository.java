package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CarRepository {

    private final JdbcTemplate jdbcTemplate;

    public CarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Car> findAll() {
        String sql = "SELECT * FROM car";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public Car findById(Long id) {
        String sql = "SELECT * FROM car WHERE car_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CarRowMapper());
    }

    public void save(Car car) {
        String sql = "INSERT INTO car (car_emission, year, brand, model, color, equipment_level, " +
                "vehicle_number, chassis_number, price, registration_fee, isAvailableForLoan, isReadyForUse, " +
                "payment_time, transport_time, image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getVehicleNumber(), car.getChassisNumber(),
                car.getPrice(), car.getRegistrationFee(),
                false, // Udlejet = nej
                false, // Ikke klar til brug endnu
                car.getPaymentTime(), car.getTransportTime(), car.getImage());
    }

    public void saveStatus(Car car) {
        String sql = "UPDATE car SET isAvailableForLoan = ?, isReadyForUse = ? WHERE car_id = ?";
        jdbcTemplate.update(sql, car.isAvailableForLoan(), car.isReadyForUse(), car.getCarId());
    }

    public void updateCarStatusAfterRental(Long carId) {
        String sql = "UPDATE car SET isAvailableForLoan = 1, isReadyForUse = 0 WHERE car_id = ?";
        jdbcTemplate.update(sql, carId);
    }

    public void resetCarAfterDamageReport(Long carId) {
        String sql = "UPDATE car SET isAvailableForLoan = 0, isReadyForUse = 0 WHERE car_id = ?";
        jdbcTemplate.update(sql, carId);
    }

    public List<Car> findNotRentedAndNotReadyCars() {
        String sql = "SELECT * FROM car WHERE isAvailableForLoan = 0 AND isReadyForUse = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findAvailableForLoan() {
        String sql = "SELECT * FROM car WHERE isAvailableForLoan = 1 AND isReadyForUse = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findRentedCars() {
        String sql = "SELECT * FROM car WHERE isAvailableForLoan = 1 AND isReadyForUse = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Find biler der er udlejet og samtidig klar til brug
    public List<Car> findRentedAndReadyCars() {
        String sql = "SELECT * FROM car WHERE isAvailableForLoan = 1 AND isReadyForUse = 1";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    private static class CarRowMapper implements RowMapper<Car> {
        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            Car car = new Car();
            car.setCarId(rs.getLong("car_id"));
            car.setCarEmission(rs.getInt("car_emission"));
            car.setYear(rs.getInt("year"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setColor(rs.getString("color"));
            car.setEquipmentLevel(rs.getString("equipment_level"));
            car.setVehicleNumber(rs.getString("vehicle_number"));
            car.setChassisNumber(rs.getString("chassis_number"));
            car.setPrice(rs.getDouble("price"));
            car.setRegistrationFee(rs.getDouble("registration_fee"));
            car.setAvailableForLoan(rs.getBoolean("isAvailableForLoan"));
            car.setReadyForUse(rs.getBoolean("isReadyForUse"));
            car.setPaymentTime(rs.getInt("payment_time"));
            return car;
        }
    }
}
