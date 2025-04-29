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

    // Hent alle udlejede biler (altså dem der ikke er tilgængelige)
    public List<Car> findRentedCars() {
        String sql = "SELECT * FROM car WHERE is_car_available = false";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Hent udlejede biler der også er klar til afhentning
    public List<Car> findRentedAndReadyCars() {
        String sql = "SELECT * FROM car WHERE is_car_available = false AND ready_for_loan = true";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findNotRentedAndNotReadyCars() {
        String sql = "SELECT * FROM car WHERE is_car_available = false AND ready_for_loan = false";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Mapper fra SQL-resultat til Car-objekt
    private static class CarRowMapper implements RowMapper<Car> {
        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            Car car = new Car();
            car.setCarId(rs.getInt("car_id"));
            car.setCarEmission(rs.getInt("car_emission"));
            car.setYear(rs.getInt("year"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setColor(rs.getString("color"));
            car.setEquipmentLevel(rs.getString("equipment_level"));
            car.setReturnAddress(rs.getString("return_address"));
            car.setVehicleNumber(rs.getString("vehicle_number"));
            car.setChassisNumber(rs.getString("chassis_number"));
            car.setPrice(rs.getDouble("price"));
            car.setRegistrationFee(rs.getDouble("registration_fee"));
            car.setCarAvailable(rs.getBoolean("is_car_available"));
            car.setReadyForLoan(rs.getBoolean("ready_for_loan"));
            car.setPaymentTime(rs.getInt("payment_time"));
            car.setTransportTime(rs.getInt("transport_time"));
            return car;
        }
    }
}
