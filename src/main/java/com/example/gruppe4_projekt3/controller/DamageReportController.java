package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DamageReportController {

    private final CarRepository carRepository;

    public DamageReportController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/damage-report")
    public String showCarsWithMissingDamageReport(Model model) {
        List<Car> cars = carRepository.findNotRentedAndNotReadyCars();
        model.addAttribute("cars", cars);
        return "damagereport";
    }

    @GetMapping("/damage-report/fill/{id}")
    public String showFillReportPage(@PathVariable Long id, Model model) {
        Car car;
        try {
            car = carRepository.findById(id);
        } catch (Exception e) {
            // Her kan du evt. logge fejlen eller sende til en fejlside
            return "error"; // eller "redirect:/damage-report" hvis du hellere vil tilbage
        }

        model.addAttribute("car", car);
        return "fill_damagereport"; // fill_damagereport.html
    }

    @PostMapping("/damage-report/fill/{id}")
    public String submitDamageReport(@PathVariable Long id, @RequestParam String report) {
        Car car;
        try {
            car = carRepository.findById(id);
        } catch (Exception e) {
            // Her kan du evt. logge fejlen eller sende til en fejlside
            return "error"; // eller "redirect:/damage-report" hvis du hellere vil tilbage
        }


        car.setDamageReport(report);
        car.setCarAvailable(true);
        car.setReadyForLoan(true);

        carRepository.save(car);
        return "redirect:/damage-report";
    }
}
