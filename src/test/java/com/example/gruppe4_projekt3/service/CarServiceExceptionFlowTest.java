package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceExceptionFlowTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car invalidCar;

    @BeforeEach
    void setUp() {
        // Opretter en bil med en tom nummerplade til at teste valideringsfejl
        invalidCar = new Car();
        invalidCar.setLicensePlate("");
        invalidCar.setYear(2023);
        invalidCar.setBrand("Toyota");
        invalidCar.setModel("Corolla");
        invalidCar.setColor("Red");
        invalidCar.setEquipmentLevel("Standard");
        invalidCar.setVehicleNumber("VN123");
        invalidCar.setChassisNumber("CN123");
        invalidCar.setPrice(200000.00);
        invalidCar.setRegistrationFee(10000.00);
        invalidCar.setImage("/img/toyota.jpg");
        invalidCar.setRented(false);
        invalidCar.setNeedsDamageReport(false);
    }

    @Test
    @DisplayName("Exception Flow: Fejl i at gemme bil med tom nummerplade variabel")
    void saveCar_EmptyLicensePlate_ThrowsIllegalArgumentException() {
        System.out.println("Starter test: saveCar_EmptyLicensePlate_ThrowsIllegalArgumentException");

        // Act & Assert: Forventer at saveCar kaster en IllegalArgumentException pga. tom nummerplade
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.saveCar(invalidCar);
        });

        // Udskriver exception-beskeden for at bekræfte, hvad der blev kastet
        System.out.println("Exception besked: " + exception.getMessage());

        // Verificerer fejlbeskeden
        assertEquals("Nummerplade må ikke være tom", exception.getMessage());

        // Verificerer at carRepository.save ikke blev kaldt, da valideringen fejlede
        verify(carRepository, never()).save(invalidCar);

        System.out.println("Test fuldført: sendte en IllegalArgumentException pga. en tom nummerplade");
    }
}