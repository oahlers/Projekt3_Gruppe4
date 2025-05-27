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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

// Exception Flow test af CarService
// [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
@ExtendWith(MockitoExtension.class)
class CarServiceHappyFlowTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car validCar;

    @BeforeEach
    void setUp() {
        // Opretter en gyldig bil til brug i testen
        validCar = new Car();
        validCar.setLicensePlate("AB12345");
        validCar.setYear(2023);
        validCar.setBrand("Toyota");
        validCar.setModel("Corolla");
        validCar.setColor("Red");
        validCar.setEquipmentLevel("Standard");
        validCar.setVehicleNumber("VN123");
        validCar.setChassisNumber("CN123");
        validCar.setPrice(200000.00);
        validCar.setRegistrationFee(10000.00);
        validCar.setImage("/img/toyota.jpg");
        validCar.setRented(false);
        validCar.setNeedsDamageReport(false);
    }

    @Test
    @DisplayName("Happy Flow: Gemnte en bil")
    void saveCar_HappyFlow_SuccessfullySavesCar() {
        System.out.println("Starter test: saveCar_HappyFlow_SuccessfullySavesCar");

        // Arrange: Konfigurerer mock til at simulere en vellykket gemning
        doNothing().when(carRepository).save(validCar);

        // Act: Kalder saveCar med en gyldig bil
        carService.saveCar(validCar);

        // Assert: Verificerer at carRepository.save blev kaldt én gang med den korrekte bil
        verify(carRepository, times(1)).save(validCar);

        // Ingen exception blev kastet, hvilket indikerer succes
        assertDoesNotThrow(() -> carService.saveCar(validCar));

        System.out.println("Test fuldført: Gemte en bil");
    }
}