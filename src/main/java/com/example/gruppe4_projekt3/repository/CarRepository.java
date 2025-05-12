package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class CarRepository {

    private final JdbcTemplate jdbcTemplate;

    public CarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Henter alle biler med evt. tilknyttet udlejningsinformation.
    public List<Car> findAll() {
        String sql = "SELECT c.*, r.customer_name, r.ready_for_use_date, c.license_plate " + // Added license_plate
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public Car findById(Long id) {
        String sql = "SELECT c.*, r.customer_name, r.ready_for_use_date, c.license_plate " + // Added license_plate
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL " +
                "WHERE c.car_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CarRowMapper());
    }

    // Gemmer en ny bil i databasen.
    public void save(Car car) {
        String sql = "INSERT INTO car (car_emission, year, brand, model, color, equipment_level, " +
                "vehicle_number, chassis_number, price, registration_fee, isAvailableForLoan, isReadyForUse, image, license_plate) " + // Added license_plate
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Added license_plate
        jdbcTemplate.update(sql,
                car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getVehicleNumber(), car.getChassisNumber(),
                car.getPrice(), car.getRegistrationFee(),
                car.isAvailableForLoan(), car.isReadyForUse(), car.getImage(), car.getLicensePlate()); // Added car.getLicensePlate()
    }


    // Markerer en bil som udlejet og opretter en ny udlejning.
    public void markAsRented(Long carId, LocalDate startDate, String customerName, String customerEmail,
                             int rentalMonths, int paymentTime, int transportTime, String subscriptionType) {

        LocalDate readyForUseDate = startDate.plusMonths(rentalMonths);

        String findSubscriptionIdSql = "SELECT id FROM subscription_type WHERE type_name = ?";
        Integer subscriptionTypeId = jdbcTemplate.queryForObject(
                findSubscriptionIdSql,
                new Object[]{subscriptionType},
                Integer.class
        );

        String rentalSql = "INSERT INTO rental (car_id, start_date, customer_name, customer_email, " +
                "rental_months, ready_for_use_date, payment_time, transport_time, subscription_type_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(rentalSql, carId, startDate, customerName, customerEmail,
                rentalMonths, readyForUseDate, paymentTime, transportTime, subscriptionTypeId);

        String carSql = "UPDATE car SET isAvailableForLoan = 1, isReadyForUse = 0 WHERE car_id = ?";
        jdbcTemplate.update(carSql, carId);
    }


    // Nulstiller en bils status efter en skadeanmeldelse.
    public void resetAfterDamageReport(Long carId) {
        String sql = "UPDATE car SET isAvailableForLoan = 0, isReadyForUse = 0 WHERE car_id = ?";
        jdbcTemplate.update(sql, carId);
    }

    // Henter alle biler der er udlejede.
    public List<Car> findRentedCars() {
        String sql = "SELECT c.*, r.customer_name, r.ready_for_use_date " +
                "FROM car c JOIN rental r ON c.car_id = r.car_id WHERE r.end_date IS NULL";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Henter alle biler der ikke er tilgængelige og ikke er klar til brug.
    public List<Car> findAvailableForLoan() {
        String sql = "SELECT * FROM car WHERE isAvailableForLoan = 0 AND isReadyForUse = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Henter alle biler mangler skaderapport men stadig er udlejede.
    public List<Car> findCarsNeedingDamageReport() {
        String sql = "SELECT c.*, r.customer_name, r.ready_for_use_date " +
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL " +
                "WHERE c.isReadyForUse = 1";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Henter alle biler der hverken er tilgængelige for lån eller mangler skaderapport.
    public List<Car> findNotRentedAndNotReadyCars() {
        String sql = "SELECT * FROM car WHERE isAvailableForLoan = 0 AND isReadyForUse = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Henter alle biler som er udlejede og mangler skaderapport.
    public List<Car> findRentedAndReadyCars() {
        String sql = "SELECT c.*, r.customer_name, r.ready_for_use_date " +
                "FROM car c JOIN rental r ON c.car_id = r.car_id " +
                "WHERE c.isAvailableForLoan = 1 AND c.isReadyForUse = 1 AND r.end_date IS NULL";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    // Cronjob der opdaterer hver dag ved midnat for at holde styr på udlejningsperioden.
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateReadyForUseStatus() {
        String sql = "SELECT car_id FROM rental WHERE ready_for_use_date <= ? AND end_date IS NULL";
        List<Long> carIds = jdbcTemplate.queryForList(sql, new Object[]{LocalDate.now()}, Long.class);

        for (Long carId : carIds) {
            String updateSql = "UPDATE car SET isReadyForUse = 1 WHERE car_id = ?";
            jdbcTemplate.update(updateSql, carId);
        }
    }

    // Beregner gennemsnitlig betalingstid for alle udlejninger.
    public Double getAveragePaymentTime() {
        String sql = "SELECT AVG(payment_time) FROM rental WHERE payment_time IS NOT NULL";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return (result != null) ? result : 0.0;
    }

    // Beregner gennemsnitlig transporttid for alle biler.
    public Double getAverageTransportTime() {
        String sql = "SELECT AVG(r.transport_time) FROM rental r JOIN car c ON r.car_id = c.car_id WHERE r.transport_time IS NOT NULL";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return (result != null) ? result : 0.0;
    }

    // Beregner gennemsnitlig lejeperiode pr. bil.
    public Double getAverageRentalDurationPerCar() {
        String sql = "SELECT AVG(r.rental_months) FROM rental r WHERE r.rental_months IS NOT NULL";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return (result != null) ? result : 0.0;
    }

    // Mapper en række fra ResultSet til et Car-objekt der inkluderer kundeinformation og resterende lejedage.
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
            car.setImage(rs.getString("image"));
            car.setLicensePlate(rs.getString("license_plate")); // Set the license plate

            try {
                String customerName = rs.getString("customer_name");
                car.setCustomerName(customerName);
            } catch (SQLException e) {
                car.setCustomerName(null);
            }

            try {
                java.sql.Date readyForUseDate = rs.getDate("ready_for_use_date");
                if (readyForUseDate != null) {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), readyForUseDate.toLocalDate());
                    car.setRemainingRentalDays(days >= 0 ? days : 0);
                } else {
                    car.setRemainingRentalDays(null);
                }
            } catch (SQLException e) {
                car.setRemainingRentalDays(null);
            }
            return car;
        }
    }

}
