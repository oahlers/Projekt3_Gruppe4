package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.*;
import com.example.gruppe4_projekt3.service.DamageReportService;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.DamageReportRepository;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import com.example.gruppe4_projekt3.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DamageReportController {
    private final DamageReportService damageReportService;
    private final CarRepository carRepository;
    private final DamageReportRepository damageReportRepository;
    private final EmployeeRepository employeeRepository;
    private final RentalService rentalService;

    public DamageReportController(DamageReportService damageReportService, CarRepository carRepository,
                                  DamageReportRepository damageReportRepository, EmployeeRepository employeeRepository,
                                  RentalService rentalService) {
        this.damageReportService = damageReportService;
        this.carRepository = carRepository;
        this.damageReportRepository = damageReportRepository;
        this.employeeRepository = employeeRepository;
        this.rentalService = rentalService;
    }

    // Opretter og gemmer en skadesrapport for en bil og viser en bekræftelsesside med prisberegning.
    @PostMapping("/damageReportDone/{id}")
    public String submitDamageReport(@PathVariable("id") Long carId,
                                     @RequestParam(name = "report", required = false) String[] reports,
                                     @RequestParam(name = "price", required = false) double[] prices,
                                     @RequestParam int mileage,
                                     @RequestParam String overallDescription,
                                     HttpSession session,
                                     Model model) {
        if (reports == null) {
            reports = new String[0];
        }
        if (prices == null) {
            prices = new double[0];
        }

        Car car = carRepository.findById(carId);
        Rental rental = damageReportRepository.findLatestRentalByCarId(carId);
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee == null) {
            employee = employeeRepository.findByEmployeeId(1);
        }

        List<Damage> damages = new ArrayList<>();
        for (int i = 0; i < reports.length; i++) {
            if (reports[i] != null && !reports[i].isBlank()) {
                Damage damage = new Damage();
                damage.setDescription(reports[i]);
                damage.setPrice(prices[i]);
                damages.add(damage);
            }
        }

        damageReportService.createDamageReport(carId, employee, rental != null ? rental.getCustomerEmail() : null, mileage, damages);

        double totalDamagePrice = damageReportService.calculateTotalDamagePrice(damages, mileage);
        double kmFee = mileage * 0.75;
        double totalPrice = totalDamagePrice;

        model.addAttribute("rental", rental);
        model.addAttribute("car", car);
        model.addAttribute("employee", employee);
        model.addAttribute("overallDescription", overallDescription);
        model.addAttribute("mileage", mileage);
        model.addAttribute("damages", damages);
        model.addAttribute("kmFee", kmFee);
        model.addAttribute("totalDamagePrice", totalDamagePrice - kmFee);
        model.addAttribute("totalPrice", totalPrice);

        return "damageReportDone";
    }

    // Viser den seneste skadesrapport for en bil sammen med prisberegninger og relaterede oplysninger.
    @GetMapping("/damageReportDone/{carId}")
    public String showDamageReport(@PathVariable Long carId, Model model) {
        DamageReport report = damageReportService.findLatestByCarId(carId);
        if (report == null) {
            model.addAttribute("errorMessage", "Ingen skadesrapport fundet for denne bil.");
            return "error";
        }
        Car car = carRepository.findById(carId);
        Rental rental = damageReportRepository.findLatestRentalByCarId(carId);

        double totalDamagePrice = report.getDamages().stream().mapToDouble(Damage::getPrice).sum();
        int mileage = report.getMileage();
        double kmFee = mileage * 0.75;
        double totalPrice = totalDamagePrice + kmFee;

        model.addAttribute("report", report);
        model.addAttribute("car", car);
        model.addAttribute("rental", rental);
        model.addAttribute("damages", report.getDamages());
        model.addAttribute("totalDamagePrice", totalDamagePrice);
        model.addAttribute("kmFee", kmFee);
        model.addAttribute("mileage", mileage);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("overallDescription", "Ikke tilgængelig");

        return "damageReportDone";
    }

    // Viser siden til at bekræfte køb af en bil for en kunde med et "Limited" abonnement.
    @GetMapping("/buyCar/{id}")
    public String showBuyCarForm(@PathVariable("id") Long carId, Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        Car car = carRepository.findById(carId);
        if (car == null || !car.isNeedsDamageReport()) {
            model.addAttribute("errorMessage", "Bilen er ikke klar til at blive behandlet.");
            return "error";
        }

        Rental rental = rentalService.findLatestRentalByCarId(carId);
        if (rental == null || rental.getSubscriptionTypeId() != 2 || rental.getReadyForUseDate() == null ||
                rental.getReadyForUseDate().isAfter(LocalDate.now()) || rental.isPurchased()) {
            model.addAttribute("errorMessage", "Denne bil kan ikke købes på nuværende tidspunkt.");
            return "error";
        }

        double totalPrice = car.getPrice() + car.getRegistrationFee();

        model.addAttribute("car", car);
        model.addAttribute("rental", rental);
        model.addAttribute("totalPrice", totalPrice);
        return "buyCar";
    }

    // Bekræfter købet af en bil og viser en bekræftelsesside.
    @PostMapping("/buyCarDone/{id}")
    public String confirmCarPurchase(@PathVariable("id") Long carId, @RequestParam("rentalId") Long rentalId, Model model) {
        Car car = carRepository.findById(carId);
        Rental rental = rentalService.findLatestRentalByCarId(carId);
        if (car == null || rental == null || rental.getRentalId() != rentalId) {
            model.addAttribute("errorMessage", "Bilen eller lejeaftalen blev ikke fundet.");
            return "error";
        }

        rentalService.markCarAsPurchased(rentalId, carId);

        double totalPrice = car.getPrice() + car.getRegistrationFee();

        model.addAttribute("car", car);
        model.addAttribute("rental", rental);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("purchaseDate", LocalDate.now());
        return "buyCarDone";
    }
}