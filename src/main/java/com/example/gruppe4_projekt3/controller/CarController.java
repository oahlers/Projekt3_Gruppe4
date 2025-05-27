package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.service.CarService;
import com.example.gruppe4_projekt3.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {
    private final CarService carService;
    private final RentalService rentalService;

    public CarController(CarService carService, RentalService rentalService) {
        this.carService = carService;
        this.rentalService = rentalService;
    }

    // Tilføjer en ny bil til systemet og redirecter til dashboardet, hvis det lykkes, eller viser en fejl.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @PostMapping("/cars/add")
    public String addCar(@ModelAttribute Car car, HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        try {
            carService.saveCar(car);
            return "dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "addCars";
        }
    }

    // Registrerer og leverer en bil til en kunde ved at oprette en lejeaftale og redirecter til dashboardet.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @PostMapping("/registerAndDeliverCar")
    public String registerDelivery(
            @RequestParam Long carId,
            @RequestParam String name,
            @RequestParam String deliveryAddress,
            @RequestParam int rentalMonths,
            @RequestParam int paymentTime,
            @RequestParam int transportTime,
            @RequestParam String email,
            @RequestParam String subscriptionType,
            @RequestParam int kilometersPerMonth,
            Model model) {
        try {
            int subscriptionTypeId = "UNLIMITED".equalsIgnoreCase(subscriptionType) ? 1 : 2;
            rentalService.createRental(carId, name, email, deliveryAddress, rentalMonths, subscriptionTypeId,
                    kilometersPerMonth, paymentTime, transportTime);
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Car> availableCars = carService.findAvailableForLoan();
            model.addAttribute("availableCars", availableCars);
            return "registerAndDeliverCar";
        }
    }

    // Viser formularen til redigering af en bil baseret på bilens ID og redirecter til detaljesiden, hvis bilen ikke findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/carOverviewEdit/{carId}")
    public String showEditForm(@PathVariable Long carId, Model model) {
        Car car = carService.findCarById(carId);
        if (car != null) {
            model.addAttribute("car", car);
            return "carOverviewEdit";
        }
        return "carOverviewDetails";
    }

    // Opdaterer en eksisterende bil med nye oplysninger og redirecter til biloversigten, eller viser en fejl.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @PostMapping("/carOverviewEdit/{carId}")
    public String updateCar(@PathVariable Long carId, @ModelAttribute Car car, HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        try {
            car.setCarId(carId);
            carService.updateCar(car);
            return "redirect:/carOverview";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("car", car);
            return "carOverviewEdit";
        }
    }
}