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

    // Henter en liste over alle biler fra databasen.
    public List<Car> findAll() {
        String sql = "SELECT * FROM car";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Finder en bil i databasen ud fra dens ID og returnerer null, hvis den ikke findes.
    public Car findById(Long id) {
        String sql = "SELECT * FROM car WHERE car_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CarRowMapper());
    }

    // Gemmer en ny bil i databasen med de angivne detaljer.
    public void save(Car car) {
        String sql = "INSERT INTO car (car_emission, year, brand, model, color, equipment_level, " +
                "vehicle_number, chassis_number, price, registration_fee, isRented, " +
                "needsDamageReport, image, license_plate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getVehicleNumber(), car.getChassisNumber(),
                car.getPrice(), car.getRegistrationFee(),
                car.isRented(), car.isNeedsDamageReport(), car.getImage(), car.getLicensePlate());
    }

    // Opdaterer en eksisterende bil i databasen med nye oplysninger baseret på dens ID.
    public void update(Car car) {
        String sql = "UPDATE car SET car_emission = ?, year = ?, brand = ?, model = ?, color = ?, " +
                "equipment_level = ?, vehicle_number = ?, chassis_number = ?, license_plate = ?, " +
                "price = ?, registration_fee = ?, image = ?, isRented = ?, needsDamageReport = ? " +
                "WHERE car_id = ?";
        jdbcTemplate.update(sql,
                car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getVehicleNumber(), car.getChassisNumber(),
                car.getLicensePlate(), car.getPrice(), car.getRegistrationFee(),
                car.getImage(), car.isRented(), car.isNeedsDamageReport(), car.getCarId());
    }

    // Nulstiller en bils tilgængelighed og skadesstatus i databasen efter en skadesrapport.
    public void resetAfterDamageReport(Long carId) {
        jdbcTemplate.update(
                "UPDATE car SET isRented = 0, needsDamageReport = 0 WHERE car_id = ?",
                carId);
    }

    // Henter en liste over biler, der er tilgængelige til udlejning (ikke udlejet og klar til brug).
    public List<Car> findAvailableForLoan() {
        String sql = "SELECT * FROM car WHERE isRented = 0 AND needsDamageReport = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Henter en liste over biler, der kræver en skadesrapport (klar til brug).
    public List<Car> findCarsNeedingDamageReport() {
        String sql = "SELECT * FROM car WHERE needsDamageReport = 1";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Beregner den gennemsnitlige tilgængelighedstid for en bil baseret på dens historik.
    public Double getAverageAvailabilityPerCar(Long carId) {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(a.duration_days) FROM availability_log a WHERE a.car_id = ?",
                new Object[]{carId}, Double.class);
        return result != null ? result : 0.0;
    }

    // Beregner den gennemsnitlige lejevarighed for en bil baseret på dens lejeaftaler.
    public Double getAverageRentalDurationPerCar(Long carId) {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(r.rental_months) FROM rental r WHERE r.car_id = ?",
                new Object[]{carId}, Double.class);
        return result != null ? result : 0.0;
    }

    // Beregner den gennemsnitlige betalingstid for alle aktive lejeaftaler.
    public Double getAveragePaymentTime() {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(payment_time) FROM rental WHERE payment_time IS NOT NULL",
                Double.class);
        return result != null ? result : 0.0;
    }

    // Beregner den gennemsnitlige transporttid for alle aktive lejeaftaler.
    public Double getAverageTransportTime() {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(transport_time) FROM rental WHERE transport_time IS NOT NULL",
                Double.class);
        return result != null ? result : 0.0;
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
            car.setRented(rs.getBoolean("isRented"));
            car.setNeedsDamageReport(rs.getBoolean("needsDamageReport"));
            car.setImage(rs.getString("image"));
            car.setLicensePlate(rs.getString("license_plate"));
            return car;
        }
    }
}