package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/cars/add")
    public String addCar(@ModelAttribute Car car, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        carRepository.save(car);
        return "dashboard";
    }

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
            @RequestParam int kilometersPerMonth) {

        carRepository.markAsRented(
                carId, LocalDate.now(), name, email, rentalMonths, paymentTime,
                transportTime, subscriptionType, deliveryAddress, kilometersPerMonth);

        return "dashboard";
    }


    // Denne metode håndterer at vise bilens redigeringsformular
    @GetMapping("/carOverviewEdit/{carId}")
    public String showEditForm(@PathVariable Long carId, Model model) {
        Optional<Car> carOpt = Optional.ofNullable(carRepository.findById(carId));
        if (carOpt.isPresent()) {
            model.addAttribute("car", carOpt.get());
            return "carOverviewEdit";
        } else {
            return "carOverviewDetails";
        }
    }

     @PostMapping("/carOverviewEdit/{carId}")
    public String updateCar(@PathVariable Long carId, @ModelAttribute Car car, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        Car existingCar = carRepository.findById(carId);

        if (existingCar != null) {
            existingCar.setChassisNumber(car.getChassisNumber());
            existingCar.setLicensePlate(car.getLicensePlate());
            existingCar.setVehicleNumber(car.getVehicleNumber());
            existingCar.setCarEmission(car.getCarEmission());
            existingCar.setYear(car.getYear());
            existingCar.setBrand(car.getBrand());
            existingCar.setModel(car.getModel());
            existingCar.setColor(car.getColor());
            existingCar.setPrice(car.getPrice());
            existingCar.setImage(car.getImage());

            carRepository.update(existingCar);
        }

        return  "redirect:/carOverview";
    }



}
