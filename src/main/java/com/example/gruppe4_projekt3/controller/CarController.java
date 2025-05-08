package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Customer;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // Viser alle biler fra databasen.
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

    // Viser alle biler og formular til at tilføje ny bil.
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

    // Viser leveringssiden med liste over tilgængelige biler.
    @GetMapping("/EmployeeLogin/deliverCar")
    public String showDeliverCarPage(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("availableCars", carRepository.findAvailableForLoan());
        return "EmployeeLogin/deliverCar";
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
    // Viser statistik over gennemsnitlig betalingstid, transporttid og lejeperiode.
    @GetMapping("/EmployeeLogin/statistics")
    public String showStatistics(Model model) {
        double averagePaymentTime = carRepository.getAveragePaymentTime();
        double averageTransportTime = carRepository.getAverageTransportTime();
        double averageRentalDuration = carRepository.getAverageRentalDurationPerCar();
        model.addAttribute("averagePaymentTime", carRepository.getAveragePaymentTime());
        model.addAttribute("averageTransportTime", carRepository.getAverageTransportTime());
        model.addAttribute("averageRentalDurationPerCar", carRepository.getAverageRentalDurationPerCar());
        return "EmployeeLogin/statistics";
    }
}
