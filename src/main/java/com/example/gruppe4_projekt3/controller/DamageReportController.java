package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
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

    public DamageReportController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/EmployeeLogin/damageReport")
    public String showDamageReportList(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Car> cars = carRepository.findNotRentedAndNotReadyCars();
        model.addAttribute("employee", loggedInEmployee);
        model.addAttribute("cars", cars);
        return "EmployeeLogin/damageReport";
    }

    @GetMapping("/EmployeeLogin/damageReportFill/{id}")
    public String showFillReportPage(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id);
        if (car == null) {
            return "error";
        }
        model.addAttribute("car", car);
        return "EmployeeLogin/damageReportConfirmation";
    }

    @PostMapping("/EmployeeLogin/damageReportFill/{id}")
    public String submitDamageReport(@PathVariable Long id, @RequestParam String report) {
        Car car = carRepository.findById(id);
        if (car == null) {
            return "error";
        }
        car.setDamageReport(report);
        car.setCarAvailable(true);
        car.setReadyForLoan(true);
        carRepository.save(car);
        return "EmployeeLogin/damageReportDone";
    }

    @GetMapping("/EmployeeLogin/damageReportDone")
    public String showDamageReportDone() {
        return "EmployeeLogin/damageReportDone";
    }

    @GetMapping("/EmployeeLogin/damageReportHistory")
    public String showDamageReportHistory(Model model) {
        List<Car> carsWithDamageReports = carRepository.findCarsWithDamageReports();
        model.addAttribute("cars", carsWithDamageReports);
        return "EmployeeLogin/damageReportHistory";
    }
}
