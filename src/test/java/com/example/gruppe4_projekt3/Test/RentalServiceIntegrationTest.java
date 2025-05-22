package com.example.gruppe4_projekt3.Test;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Rental;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.RentalRepository;
import com.example.gruppe4_projekt3.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class RentalServiceIntegrationTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Ryd databasen
        jdbcTemplate.update("DELETE FROM rental");
        jdbcTemplate.update("DELETE FROM car");

        // Opret testdata
        Car car = new Car();
        car.setCarId(1L);
        car.setCarEmission(0);
        car.setYear(2023);
        car.setBrand("Test");
        car.setModel("Model");
        car.setColor("Blue");
        car.setEquipmentLevel("Standard");
        car.setVehicleNumber("TEST123");
        car.setChassisNumber("CHASSIS123");
        car.setLicensePlate("TEST1234");
        car.setPrice(200000.00);
        car.setRegistrationFee(10000.00);
        car.setRented(true);
        car.setNeedsDamageReport(false);
        carRepository.save(car);

        Rental rental = new Rental();
        rental.setRentalId(1L);
        rental.setCarId(1L);
        rental.setStartDate(LocalDate.of(2025, 1, 1));
        rental.setReadyForUseDate(LocalDate.of(2025, 5, 20));
        rental.setCustomerName("Test Customer");
        rental.setCustomerEmail("test@example.com");
        rental.setDeliveryAddress("Test Address");
        rental.setRentalMonths(5);
        rental.setPaymentTime(30);
        rental.setTransportTime(5);
        rental.setSubscriptionTypeId(2);
        rental.setMileage(1000);
        rental.setPurchased(false);
        rentalRepository.save(rental);
    }

    @Test
    void markCarAsPurchased_deletesCarAndRelatedData() {
        // Act
        rentalService.markCarAsPurchased(1L, 1L);

        // Assert
        Car deletedCar = carRepository.findById(1L);
        assertNull(deletedCar);

        Rental deletedRental = rentalRepository.findLatestByCarId(1L);
        assertNull(deletedRental);

        Integer rentalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rental WHERE car_id = ?", Integer.class, 1L);
        assertEquals(0, rentalCount);
    }
}