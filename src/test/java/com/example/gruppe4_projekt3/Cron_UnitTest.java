package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Rental;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class RentalServiceTest {

    private RentalRepository rentalRepository;
    private CarRepository carRepository;
    private JdbcTemplate jdbcTemplate;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);
        carRepository = mock(CarRepository.class);
        jdbcTemplate = mock(JdbcTemplate.class);
        rentalService = new RentalService(rentalRepository, carRepository, jdbcTemplate);
    }

    @Test
    void testUpdateReadyForUseStatus_shouldUpdateWhenReadyDatePassed() {
        // Arrange
        Rental rental1 = new Rental();
        rental1.setId(1L);
        rental1.setReadyForUse(false);
        rental1.setReadyForUseDate(LocalDate.now().minusDays(1));

        Rental rental2 = new Rental();
        rental2.setId(2L);
        rental2.setReadyForUse(false);
        rental2.setReadyForUseDate(LocalDate.now().plusDays(2)); // Not ready

        Rental rental3 = new Rental();
        rental3.setId(3L);
        rental3.setReadyForUse(true); // Already ready
        rental3.setReadyForUseDate(LocalDate.now().minusDays(5));

        List<Rental> activeRentals = Arrays.asList(rental1, rental2, rental3);

        when(rentalRepository.findAllActive()).thenReturn(activeRentals);

        // Act
        rentalService.updateReadyForUseStatus();

        // Assert
        verify(rentalRepository, times(1)).update(rental1); // Should update
        verify(rentalRepository, never()).update(rental2);  // Should not update
        verify(rentalRepository, never()).update(rental3);  // Already ready
    }
}
