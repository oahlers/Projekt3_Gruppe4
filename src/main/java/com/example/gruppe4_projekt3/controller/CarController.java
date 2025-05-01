package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;

import com.example.gruppe4_projekt3.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 import java.util.List;

@Controller
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/rented")
    public List<Car> getRentedCars() {
        return carRepository.findRentedCars();
    }

    @GetMapping("/rented/ready")
    public List<Car> getRentedAndReadyCars() {
        return carRepository.findRentedAndReadyCars();
    }

    @GetMapping("/damage-report")
    public List<Car> getCarsForDamageReport() {
        return carRepository.findNotRentedAndNotReadyCars();
    }

    @GetMapping("/EmployeeLogin/viewAllCarsAndAddCar")
    public String viewAllCars(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "EmployeeLogin/viewAllCarsAndAddCar";
    }


    @PostMapping("/cars/add")
    public String addCar(@ModelAttribute Car car) {
        carRepository.save(car);
        return "redirect:/EmployeeLogin/viewAllCarsAndAddCar";
    }
}