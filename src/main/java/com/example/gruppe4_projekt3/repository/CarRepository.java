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

    public List<Car> findAll() {
        String sql = "SELECT c.*, r.customer_name, r.start_date, r.transport_time, " +
                "r.ready_for_use_date, r.customer_email, r.delivery_address " +
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public Car findById(Long id) {
        String sql = "SELECT c.*, r.customer_name, r.start_date, r.transport_time, " +
                "r.ready_for_use_date, r.customer_email, r.delivery_address " +
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL " +
                "WHERE c.car_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CarRowMapper());
    }

    public void save(Car car) {
        String sql = "INSERT INTO car (car_emission, year, brand, model, color, equipment_level, " +
                "vehicle_number, chassis_number, price, registration_fee, isAvailableForLoan, " +
                "isReadyForUse, image, license_plate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getVehicleNumber(), car.getChassisNumber(),
                car.getPrice(), car.getRegistrationFee(),
                car.isAvailableForLoan(), car.isReadyForUse(), car.getImage(), car.getLicensePlate());
    }

    public void update(Car car) {
        String sql = "UPDATE car SET car_emission = ?, year = ?, brand = ?, model = ?, color = ?, " +
                "equipment_level = ?, vehicle_number = ?, chassis_number = ?, license_plate = ?, " +
                "price = ?, registration_fee = ?, image = ?, isAvailableForLoan = ?, isReadyForUse = ? " +
                "WHERE car_id = ?";
        jdbcTemplate.update(sql,
                car.getCarEmission(), car.getYear(), car.getBrand(), car.getModel(),
                car.getColor(), car.getEquipmentLevel(), car.getVehicleNumber(), car.getChassisNumber(),
                car.getLicensePlate(), car.getPrice(), car.getRegistrationFee(),
                car.getImage(), car.isAvailableForLoan(), car.isReadyForUse(), car.getCarId());
    }

    public void markAsRented(Long carId, LocalDate startDate, String customerName, String customerEmail,
                             int rentalMonths, int paymentTime, int transportTime, String subscriptionType,
                             String deliveryAddress, int mileage) {
        LocalDate readyForUseDate = startDate.plusMonths(rentalMonths);

        Integer subscriptionTypeId = jdbcTemplate.queryForObject(
                "SELECT id FROM subscription_type WHERE type_name = ?",
                new Object[]{subscriptionType},
                Integer.class
        );

        jdbcTemplate.update(
                "INSERT INTO rental (car_id, start_date, customer_name, customer_email, delivery_address, " +
                        "rental_months, ready_for_use_date, payment_time, transport_time, subscription_type_id, " +
                        "mileage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                carId, startDate, customerName, customerEmail, deliveryAddress,
                rentalMonths, readyForUseDate, paymentTime, transportTime, subscriptionTypeId, mileage);

        jdbcTemplate.update(
                "UPDATE car SET isAvailableForLoan = 1, isReadyForUse = 0 WHERE car_id = ?",
                carId);
    }

    public void resetAfterDamageReport(Long carId) {
        jdbcTemplate.update(
                "UPDATE car SET isAvailableForLoan = 0, isReadyForUse = 0 WHERE car_id = ?",
                carId);
    }

    public List<Car> findRentedCars() {
        String sql = "SELECT c.*, r.customer_name, r.start_date, r.transport_time, " +
                "r.ready_for_use_date, r.customer_email, r.delivery_address " +
                "FROM car c JOIN rental r ON c.car_id = r.car_id WHERE r.end_date IS NULL";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findAvailableForLoan() {
        String sql = "SELECT c.*, r.customer_name, r.customer_email, r.delivery_address, " +
                "r.start_date, r.transport_time, r.ready_for_use_date " +
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL " +
                "WHERE c.isAvailableForLoan = 0 AND c.isReadyForUse = 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findCarsNeedingDamageReport() {
        String sql = "SELECT c.*, r.customer_name, r.start_date, r.transport_time, " +
                "r.ready_for_use_date, r.customer_email, r.delivery_address " +
                "FROM car c LEFT JOIN rental r ON c.car_id = r.car_id AND r.end_date IS NULL " +
                "WHERE c.isReadyForUse = 1";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findNotRentedAndNotReadyCars() {
        return jdbcTemplate.query(
                "SELECT * FROM car WHERE isAvailableForLoan = 0 AND isReadyForUse = 0",
                new CarRowMapper());
    }

    public List<Car> findRentedAndReadyCars() {
        String sql = "SELECT c.*, r.customer_name, r.start_date, r.transport_time, " +
                "r.ready_for_use_date, r.customer_email, r.delivery_address " +
                "FROM car c JOIN rental r ON c.car_id = r.car_id " +
                "WHERE c.isAvailableForLoan = 1 AND c.isReadyForUse = 1 AND r.end_date IS NULL";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateReadyForUseStatus() {
        List<Long> carIds = jdbcTemplate.queryForList(
                "SELECT car_id FROM rental WHERE ready_for_use_date <= ? AND end_date IS NULL",
                new Object[]{LocalDate.now()},
                Long.class);

        carIds.forEach(carId -> jdbcTemplate.update(
                "UPDATE car SET isReadyForUse = 1 WHERE car_id = ?",
                carId));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void trackAvailability() {
        findAll().forEach(car -> {
            Long carId = car.getCarId();
            boolean isAvailable = car.isAvailableForLoan();
            int count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM availability_tracking WHERE car_id = ?",
                    Integer.class,
                    carId);

            if (isAvailable && count == 0) {
                jdbcTemplate.update(
                        "INSERT INTO availability_tracking (car_id, start_date) VALUES (?, ?)",
                        carId, LocalDate.now());
            } else if (!isAvailable && count > 0) {
                LocalDate startDate = jdbcTemplate.queryForObject(
                        "SELECT start_date FROM availability_tracking WHERE car_id = ?",
                        (rs, rowNum) -> rs.getDate("start_date").toLocalDate(),
                        carId);
                long duration = ChronoUnit.DAYS.between(startDate, LocalDate.now());

                jdbcTemplate.update(
                        "INSERT INTO availability_log (car_id, start_date, end_date, duration_days) VALUES (?, ?, ?, ?)",
                        carId, startDate, LocalDate.now(), duration);

                jdbcTemplate.update(
                        "DELETE FROM availability_tracking WHERE car_id = ?",
                        carId);
            }
        });
    }

    public Double getAverageAvailabilityPerCar(Long carId) {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(a.duration_days) FROM availability_log a WHERE a.car_id = ?",
                new Object[]{carId},
                Double.class);
        return result != null ? result : 0.0;
    }

    public Double getAverageRentalDurationPerCar(Long carId) {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(r.rental_months) FROM rental r WHERE r.car_id = ?",
                new Object[]{carId},
                Double.class);
        return result != null ? result : 0.0;
    }

    public Double getAveragePaymentTime() {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(payment_time) FROM rental WHERE payment_time IS NOT NULL",
                Double.class);
        return result != null ? result : 0.0;
    }

    public Double getAverageTransportTime() {
        Double result = jdbcTemplate.queryForObject(
                "SELECT AVG(r.transport_time) FROM rental r JOIN car c ON r.car_id = c.car_id WHERE r.transport_time IS NOT NULL",
                Double.class);
        return result != null ? result : 0.0;
    }

    public List<Car> findRentedCarsWithDetails() {
        String sql = "SELECT c.*, r.customer_name, r.customer_email, r.start_date, r.transport_time, " +
                "r.delivery_address, r.ready_for_use_date " +
                "FROM car c JOIN rental r ON c.car_id = r.car_id " +
                "WHERE r.end_date IS NULL AND DATE_ADD(r.start_date, INTERVAL r.transport_time DAY) >= CURRENT_DATE " +
                "ORDER BY DATE_ADD(r.start_date, INTERVAL r.transport_time DAY) ASC";
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
            car.setImage(rs.getString("image"));
            car.setLicensePlate(rs.getString("license_plate"));
            car.setCustomerName(rs.getString("customer_name"));
            car.setCustomerEmail(rs.getString("customer_email"));
            car.setDeliveryAddress(rs.getString("delivery_address"));

            LocalDate startDate = rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null;
            Integer transportTime = rs.getObject("transport_time") != null ? rs.getInt("transport_time") : null;

            car.setStartDate(startDate);
            car.setTransportTime(transportTime);

            if (startDate != null && transportTime != null) {
                LocalDate deliveryDate = startDate.plusDays(transportTime);
                long daysUntilDelivery = ChronoUnit.DAYS.between(LocalDate.now(), deliveryDate);
                car.setRemainingRentalDays(daysUntilDelivery >= 0 ? daysUntilDelivery : null);
            } else {
                car.setRemainingRentalDays(null);
            }

            return car;
        }
    }
}
