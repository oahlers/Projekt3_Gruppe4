package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Rental;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RentalRepository {
    private final JdbcTemplate jdbcTemplate;

    public RentalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Gemmer en ny lejeaftale i databasen med de angivne oplysninger.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void save(Rental rental) {
        String sql = "INSERT INTO rental (car_id, customer_name, customer_email, delivery_address, " +
                "rental_months, start_date, ready_for_use_date, subscription_type_id, mileage, " +
                "payment_time, transport_time, isPurchased) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                rental.getCarId(), rental.getCustomerName(), rental.getCustomerEmail(),
                rental.getDeliveryAddress(), rental.getRentalMonths(), LocalDate.now(),
                LocalDate.now().plusMonths(rental.getRentalMonths()), rental.getSubscriptionTypeId(),
                rental.getMileage(), rental.getPaymentTime(), rental.getTransportTime(), rental.isPurchased());
    }

    // Finder den seneste lejeaftale for en given bil og returnerer null, hvis ingen findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Rental findLatestByCarId(Long carId) {
        String sql = "SELECT * FROM rental WHERE car_id = ? ORDER BY start_date DESC LIMIT 1";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Rental rental = new Rental();
            rental.setRentalId(rs.getLong("rental_id"));
            rental.setCarId(rs.getLong("car_id"));
            rental.setStartDate(rs.getObject("start_date", LocalDate.class));
            rental.setEndDate(rs.getObject("end_date", LocalDate.class));
            rental.setCustomerName(rs.getString("customer_name"));
            rental.setCustomerEmail(rs.getString("customer_email"));
            rental.setDeliveryAddress(rs.getString("delivery_address"));
            rental.setRentalMonths(rs.getInt("rental_months"));
            rental.setReadyForUseDate(rs.getObject("ready_for_use_date", LocalDate.class));
            rental.setPaymentTime(rs.getObject("payment_time", Integer.class));
            rental.setTransportTime(rs.getObject("transport_time", Integer.class));
            rental.setSubscriptionTypeId(rs.getInt("subscription_type_id"));
            rental.setMileage(rs.getInt("mileage"));
            rental.setPurchased(rs.getBoolean("isPurchased"));
            return rental;
        }, carId).stream().findFirst().orElse(null);
    }

    // Henter en liste over alle aktive lejeaftaler, hvor slutdatoen ikke er sat.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<Rental> findAllActive() {
        String sql = "SELECT * FROM rental WHERE end_date IS NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Rental rental = new Rental();
            rental.setRentalId(rs.getLong("rental_id"));
            rental.setCarId(rs.getLong("car_id"));
            rental.setStartDate(rs.getObject("start_date", LocalDate.class));
            rental.setEndDate(rs.getObject("end_date", LocalDate.class));
            rental.setCustomerName(rs.getString("customer_name"));
            rental.setCustomerEmail(rs.getString("customer_email"));
            rental.setDeliveryAddress(rs.getString("delivery_address"));
            rental.setRentalMonths(rs.getInt("rental_months"));
            rental.setReadyForUseDate(rs.getObject("ready_for_use_date", LocalDate.class));
            rental.setPaymentTime(rs.getObject("payment_time", Integer.class));
            rental.setTransportTime(rs.getObject("transport_time", Integer.class));
            rental.setSubscriptionTypeId(rs.getInt("subscription_type_id"));
            rental.setMileage(rs.getInt("mileage"));
            rental.setPurchased(rs.getBoolean("isPurchased"));
            return rental;
        });
    }

    // Afslutter en lejeaftale ved at sætte en slutdato i databasen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void endRental(Long rentalId, LocalDate endDate) {
        String sql = "UPDATE rental SET end_date = ? WHERE rental_id = ?";
        jdbcTemplate.update(sql, endDate, rentalId);
    }

    // Markerer en lejeaftale som købt og sætter en slutdato i databasen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void markAsPurchased(Long rentalId, LocalDate endDate) {
        String sql = "UPDATE rental SET isPurchased = 1, end_date = ? WHERE rental_id = ?";
        jdbcTemplate.update(sql, endDate, rentalId);
    }
}