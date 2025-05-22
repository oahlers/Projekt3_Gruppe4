package com.example.gruppe4_projekt3.Test;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Rental;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.RentalRepository;
import com.example.gruppe4_projekt3.service.SchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SchedulerService schedulerService;

    private Rental rental;
    private Car car;

    @BeforeEach
    void setUp() {
        rental = new Rental();
        rental.setRentalId(1L);
        rental.setCarId(1L);
        rental.setReadyForUseDate(LocalDate.of(2025, 5, 20)); // Før i dag (22. maj 2025)

        car = new Car();
        car.setCarId(1L);
        car.setNeedsDamageReport(false);
    }

    @Test
    void updateReadyForUseStatus_updatesCarNeedsDamageReport_whenRentalIsExpired() {
        // Arrange
        List<Rental> activeRentals = Arrays.asList(rental);
        when(rentalRepository.findAllActive()).thenReturn(activeRentals);

        // Act
        schedulerService.updateReadyForUseStatus();

        // Assert
        verify(carRepository).update(argThat(c -> c.getCarId().equals(1L) && c.isNeedsDamageReport()));
    }

    @Test
    void updateReadyForUseStatus_doesNotUpdateCar_whenRentalIsNotExpired() {
        // Arrange
        rental.setReadyForUseDate(LocalDate.of(2025, 5, 23)); // Efter i dag (22. maj 2025)
        List<Rental> activeRentals = Arrays.asList(rental);
        when(rentalRepository.findAllActive()).thenReturn(activeRentals);

        // Act
        schedulerService.updateReadyForUseStatus();

        // Assert
        verify(carRepository, never()).update(any(Car.class));
    }
}