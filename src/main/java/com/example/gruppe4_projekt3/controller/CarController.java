package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Customer;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.List;

// Carcontroller. håndterer POST- og GET anmodninger for bilers funktioner
// addCar, getRentedCars, getRentedAndReadyCars, getCarsForDamageReport, registerDelivery

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // Tilføjer en ny bil til databasen via formular og returnerer til dashboard.
    @PostMapping("/cars/add")
    public String addCar(@ModelAttribute Car car, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        carRepository.save(car);
        return "EmployeeLogin/dashboard";
    }

    // Returnerer en liste over alle aktuelt udlejede biler.
    @GetMapping("/rented")
    public List<Car> getRentedCars() {
        return carRepository.findRentedCars();
    }

    // Returnerer en liste over udlejede og mangel af skaderapport biler.
    @GetMapping("/rented/ready")
    public List<Car> getRentedAndReadyCars() {
        return carRepository.findRentedAndReadyCars();
    }

    // Returnerer en liste over biler der ikke er udlejede og mangler skaderapport.
    @GetMapping("/damage-report")
    public List<Car> getCarsForDamageReport() {
        return carRepository.findNotRentedAndNotReadyCars();
    }

    // Registrerer udlejning af en bil til en kunde og opdaterer bilens status.
    @PostMapping("/EmployeeLogin/deliverCar")
    public String registerDelivery(
            @RequestParam Long carId,
            @RequestParam String name,
            @RequestParam String deliveryAddress,
            @RequestParam int rentalMonths,
            @RequestParam int paymentTime,
            @RequestParam int transportTime,
            @RequestParam String email) {

        Customer customer = new Customer();
        customer.setName(name);
        customer.setDeliveryAddress(deliveryAddress);
        customer.setRentalMonths(rentalMonths);

        carRepository.markAsRented(carId, LocalDate.now(), name, email, rentalMonths, paymentTime, transportTime);

        return "redirect:/EmployeeLogin/dashboard";
    }
}