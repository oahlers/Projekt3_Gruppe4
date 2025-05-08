package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.DamageReportRepository;
import jakarta.servlet.http.HttpSession;
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
    private final DamageReportRepository damageReportRepository;

    public DamageReportController(CarRepository carRepository, DamageReportRepository damageReportRepository) {
        this.carRepository = carRepository;
        this.damageReportRepository = damageReportRepository;
    }

    // Viser en liste over biler der mangler skadesrapport
    @GetMapping("/EmployeeLogin/damageReport")
    public String showDamageReportList(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Car> cars = carRepository.findCarsNeedingDamageReport();
        model.addAttribute("employee", loggedInEmployee);
        model.addAttribute("cars", cars);
        return "EmployeeLogin/damageReport";
    }

    // Viser siden for at udfylde en skadesrapport for en bestemt bil.
    @GetMapping("/EmployeeLogin/damageReportFill/{id}")
    public String showFillReportPage(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id);
        if (car == null || !car.isReadyForUse()) {
            return "error";
        }
        model.addAttribute("car", car);
        return "EmployeeLogin/damageReportConfirmation";
    }

    // Behandler og gemmer den indsendte skadesrapport for en bil ud fra unik id.
    @PostMapping("/EmployeeLogin/damageReportFill/{id}")
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

        return "EmployeeLogin/damageReportDone";
    }

    // Viser bekræftelsessiden efter indsendelse af skadesrapport.
    @GetMapping("/EmployeeLogin/damageReportDone")
    public String showDamageReportDone() {
        return "EmployeeLogin/damageReportDone";
    }

    // Viser historikken over alle tidligere skadesrapporter.
    @GetMapping("/EmployeeLogin/damageReportHistory")
    public String showDamageReportHistory(Model model) {
        List<DamageReport> damageReports = damageReportRepository.findAll();
        model.addAttribute("damageReports", damageReports);
        return "EmployeeLogin/damageReportHistory";
    }
}