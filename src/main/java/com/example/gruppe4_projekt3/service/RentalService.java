package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Rental;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.RentalRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final JdbcTemplate jdbcTemplate;

    public RentalService(RentalRepository rentalRepository, CarRepository carRepository, JdbcTemplate jdbcTemplate) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Opretter en ny lejeaftale, validerer abonnementsregler og opdaterer bilens status til udlejet.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void createRental(Long carId, String customerName, String customerEmail, String deliveryAddress,
                             int rentalMonths, int subscriptionTypeId, int mileage, int paymentTime, int transportTime) {
        validateRental(rentalMonths, subscriptionTypeId);
        Rental rental = new Rental();
        rental.setCarId(carId);
        rental.setCustomerName(customerName);
        rental.setCustomerEmail(customerEmail);
        rental.setDeliveryAddress(deliveryAddress);
        rental.setRentalMonths(rentalMonths);
        rental.setSubscriptionTypeId(subscriptionTypeId);
        rental.setMileage(mileage);
        rental.setPaymentTime(paymentTime);
        rental.setTransportTime(transportTime);
        rental.setStartDate(LocalDate.now());
        rental.setReadyForUseDate(LocalDate.now().plusMonths(rentalMonths));
        rental.setPurchased(false);
        rentalRepository.save(rental);

        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new IllegalArgumentException("Bil med ID " + carId + " findes ikke");
        }
        car.setRented(true);
        car.setNeedsDamageReport(false);
        carRepository.update(car);
    }

    // Validerer regler for en lejeaftale baseret på abonnementsype og lejeperiode.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    private void validateRental(int rentalMonths, int subscriptionTypeId) {
        if (subscriptionTypeId != 1 && subscriptionTypeId != 2) {
            throw new IllegalArgumentException("Ugyldig abonnementstype");
        }
        if (subscriptionTypeId == 2 && rentalMonths != 5) {
            throw new IllegalArgumentException("LIMITED abonnement skal være 5 måneder");
        }
        if (subscriptionTypeId == 1 && !Arrays.asList(3, 6, 9, 12, 24, 36).contains(rentalMonths)) {
            throw new IllegalArgumentException("Ugyldig lejeperiode for UNLIMITED abonnement");
        }
    }

    // Finder den seneste lejeaftale for en given bil og returnerer null, hvis ingen findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Rental findLatestRentalByCarId(Long carId) {
        return rentalRepository.findLatestByCarId(carId);
    }

    // Henter en liste over alle aktive lejeaftaler i systemet.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<Rental> findAllActive() {
        return rentalRepository.findAllActive();
    }

    // Markerer en lejeaftale som købt, afslutter lejeaftalen og sletter bilen og relaterede data fra databasen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void markCarAsPurchased(Long rentalId, Long carId) {
        rentalRepository.markAsPurchased(rentalId, LocalDate.now());
        List<Long> reportIds = jdbcTemplate.queryForList(
                "SELECT report_id FROM damage_report WHERE car_id = ?", Long.class, carId);
        for (Long reportId : reportIds) {
            jdbcTemplate.update("DELETE FROM damage WHERE report_id = ?", reportId);
        }
        jdbcTemplate.update("DELETE FROM damage_report WHERE car_id = ?", carId);
        jdbcTemplate.update("DELETE FROM rental WHERE car_id = ?", carId);
        jdbcTemplate.update("DELETE FROM availability_log WHERE car_id = ?", carId);
        jdbcTemplate.update("DELETE FROM AvailabilityTracking WHERE car_id = ?", carId);
        jdbcTemplate.update("DELETE FROM car WHERE car_id = ?", carId);
    }

    // Beregner den gennemsnitlige betalingstid for alle aktive lejeaftaler.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Double getAveragePaymentTime() {
        return rentalRepository.findAllActive().stream()
                .mapToInt(r -> r.getPaymentTime() != null ? r.getPaymentTime() : 0)
                .average()
                .orElse(0.0);
    }

    // Beregner den gennemsnitlige transporttid for alle aktive lejeaftaler.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Double getAverageTransportTime() {
        return rentalRepository.findAllActive().stream()
                .mapToInt(r -> r.getTransportTime() != null ? r.getTransportTime() : 0)
                .average()
                .orElse(0.0);
    }
}