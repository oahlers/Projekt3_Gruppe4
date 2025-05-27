package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Henter en liste over alle biler i systemet fra databasen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    // Finder en bil ud fra dens ID og returnerer null, hvis den ikke findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Car findCarById(Long id) {
        return carRepository.findById(id);
    }

    // Gemmer en ny bil i databasen efter validering af dens oplysninger.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void saveCar(Car car) {
        validateCar(car);
        carRepository.save(car);
    }

    // Opdaterer en eksisterende bil i databasen efter validering af dens oplysninger.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void updateCar(Car car) {
        validateCar(car);
        carRepository.update(car);
    }

    // Henter en liste over biler, der er tilgængelige til udlejning.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<Car> findAvailableForLoan() {
        return carRepository.findAvailableForLoan();
    }

    // Henter en liste over biler, der kræver en skadesrapport.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<Car> findCarsNeedingDamageReport() {
        return carRepository.findCarsNeedingDamageReport();
    }

    // Validerer en bils oplysninger og kaster en undtagelse, hvis de er ugyldige.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    private void validateCar(Car car) {
        if (car.getLicensePlate() == null || car.getLicensePlate().isEmpty()) {
            throw new IllegalArgumentException("Nummerplade må ikke være tom");
        }
        if (car.getYear() < 1900 || car.getYear() > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Ugyldigt årstal");
        }
    }

    // Henter den gennemsnitlige tilgængelighedstid for en bil baseret på dens historik.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Double getAverageAvailabilityPerCar(Long carId) {
        return carRepository.getAverageAvailabilityPerCar(carId);
    }

    // Henter den gennemsnitlige lejevarighed for en bil baseret på dens lejeaftaler.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Double getAverageRentalDurationPerCar(Long carId) {
        return carRepository.getAverageRentalDurationPerCar(carId);
    }
}