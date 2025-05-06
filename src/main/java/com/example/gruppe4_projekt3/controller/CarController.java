package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Customer;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/EmployeeLogin/allCars")
    public String getAllCars(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        model.addAttribute("employee", loggedInEmployee);
        return "EmployeeLogin/allCars";
    }

    @PostMapping("/cars/add")
    public String addCar(@ModelAttribute Car car, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        carRepository.save(car);
        return "redirect:/EmployeeLogin/rentedCars";
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
    public String viewAllCars(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("employee", loggedInEmployee);
        return "EmployeeLogin/viewAllCarsAndAddCar";
    }

    @GetMapping("/EmployeeLogin/deliverCar")
    public String showDeliverCarPage(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("availableCars", carRepository.findAvailableForLoan());
        return "EmployeeLogin/deliverCar";
    }

    @PostMapping("/EmployeeLogin/deliverCar")
    public String registerDelivery(
            @RequestParam Long carId,
            @RequestParam String name,
            @RequestParam String deliveryAddress,
            @RequestParam int rentalMonths) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setDeliveryAddress(deliveryAddress);
        customer.setRentalMonths(rentalMonths);

        carRepository.markAsRented(carId, LocalDate.now(), customer.getName(), rentalMonths);

        return "redirect:/EmployeeLogin/dashboard";
    }
}