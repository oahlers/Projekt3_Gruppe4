package com.example.gruppe4_projekt3.Test;

import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.RentalRepository;
import com.example.gruppe4_projekt3.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        // Setup kan udvides, hvis nødvendigt
    }

    @Test
    void markCarAsPurchased_marksRentalAsPurchasedAndDeletesCar() {
        // Arrange
        Long rentalId = 1L;
        Long carId = 1L;
        when(jdbcTemplate.queryForList("SELECT report_id FROM damage_report WHERE car_id = ?", Long.class, carId))
                .thenReturn(Arrays.asList());

        // Act
        rentalService.markCarAsPurchased(rentalId, carId);

        // Assert
        verify(rentalRepository).markAsPurchased(rentalId, LocalDate.now());
        verify(jdbcTemplate).update("DELETE FROM damage_report WHERE car_id = ?", carId);
        verify(jdbcTemplate).update("DELETE FROM rental WHERE car_id = ?", carId);
        verify(jdbcTemplate).update("DELETE FROM availability_log WHERE car_id = ?", carId);
        verify(jdbcTemplate).update("DELETE FROM AvailabilityTracking WHERE car_id = ?", carId);
        verify(jdbcTemplate).update("DELETE FROM car WHERE car_id = ?", carId);
    }
}