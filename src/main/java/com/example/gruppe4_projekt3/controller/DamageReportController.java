package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.DamageReportRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// DamageReportController. håndterer POST- og GET anmodninger for skaderapport funktioner
// submitDamageReport

@Controller
public class DamageReportController {

    private final CarRepository carRepository;
    private final DamageReportRepository damageReportRepository;

    public DamageReportController(CarRepository carRepository, DamageReportRepository damageReportRepository) {
        this.carRepository = carRepository;
        this.damageReportRepository = damageReportRepository;
    }

    // Behandler og gemmer den indsendte skadesrapport for en bil ud fra unik id.
    @PostMapping("/damageReportFill/{id}")
    public String submitDamageReport(@PathVariable Long id,
                                     @RequestParam String report,
                                     @RequestParam Double price,
                                     @RequestParam String customerEmail,
                                     HttpSession session) {
        Car car = carRepository.findById(id);
        if (car == null || !car.isReadyForUse()) {
            return "error";
        }

        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        DamageReport damageReport = new DamageReport(car, price, loggedInEmployee, customerEmail, report);
        damageReportRepository.save(damageReport);

        return "damageReportDone";
    }
}