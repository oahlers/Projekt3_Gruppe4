package com.example.gruppe4_projekt3.repository;
import com.example.gruppe4_projekt3.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CarRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Henter biler baseret på deres tilgængelighed (f.eks. ledig eller ikke ledig)
    public List<Car> findByAvailability(boolean isCarAvailable) {
        String query = """
            SELECT car_id, car_emission, year, brand, model, color, equipment_level, return_address,
                   vehicle_number, chassis_number, price, registration_fee, is_car_available
            FROM car
            WHERE is_car_available = ?
        """;
        return jdbcTemplate.query(query, new Object[]{isCarAvailable}, new CarRowMapper());
    }

    // Gemmer en ny bil i databasen
    public void save(Car car) {
        String query = """
            INSERT INTO car (car_emission, year, brand, model, color, equipment_level, return_address,
                             vehicle_number, chassis_number, price, registration_fee, is_car_available)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(query, car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getReturnAddress(), car.getVehicleNumber(),
                car.getChassisNumber(), car.getPrice(), car.getRegistrationFee(), car.isCarAvailable());
    }

    // Opdaterer en eksisterende bil i databasen
    public void update(Car updatedCar) {
        String query = """
            UPDATE car SET car_emission = ?, year = ?, brand = ?, model = ?, color = ?, equipment_level = ?, 
                return_address = ?, vehicle_number = ?, chassis_number = ?, price = ?, registration_fee = ?, 
                is_car_available = ? WHERE car_id = ?
        """;
        jdbcTemplate.update(query, updatedCar.getCarEmission(), updatedCar.getYear(), updatedCar.getBrand(),
                updatedCar.getModel(), updatedCar.getColor(), updatedCar.getEquipmentLevel(), updatedCar.getReturnAddress(),
                updatedCar.getVehicleNumber(), updatedCar.getChassisNumber(), updatedCar.getPrice(),
                updatedCar.getRegistrationFee(), updatedCar.isCarAvailable(), updatedCar.getCarId());
    }

    // Henter alle biler fra databasen
    public List<Car> getAllCars() {
        String query = "SELECT * FROM car";
        return jdbcTemplate.query(query, new CarRowMapper());
    }

    // Henter en bil baseret på dens ID
    public Car findById(int carId) {
        String query = "SELECT * FROM car WHERE car_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{carId}, new CarRowMapper());
    }

    // RowMapper til at konvertere ResultSet til et Car-objekt
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
            return car;
        }
    }
}
