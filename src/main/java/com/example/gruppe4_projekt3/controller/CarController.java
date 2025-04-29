package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;

import com.example.gruppe4_projekt3.repository.CarRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/rented")
    public List<Car> getRentedCars() {
        return carRepository.findRentedCars();
    }

    @GetMapping("/EmployeeLogin/carOverviewEmployee")
    public String showRentedCars(Model model) {
        List<Car> rentedCars = carRepository.findRentedCars();
        model.addAttribute("rentedCars", rentedCars);
        return "car-overview-employee";
    }


    @GetMapping("/rented/ready")
    public List<Car> getRentedAndReadyCars() {
        return carRepository.findRentedAndReadyCars();
    }

    @GetMapping("/damage-report")
    public List<Car> getCarsForDamageReport() {
        return carRepository.findNotRentedAndNotReadyCars();
    }
}
