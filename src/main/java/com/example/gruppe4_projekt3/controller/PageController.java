package com.example.gruppe4_projekt3.controller;
import com.example.gruppe4_projekt3.repository.CarRepository;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CarRepository carRepository;

    // Viser hjemmesiden (index)
    @GetMapping("/")
    public String showIndexPage() {
        return "Homepage/index"; // Matcher templates/Homepage/index.html
    }

    // Viser index-siden efter registrering
    @GetMapping("/auth")
    public String showAuthPage() {
        return "Homepage/index"; // Matcher templates/Homepage/index.html
    }

    @GetMapping("EmployeeLogin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("employee", loggedInEmployee);
        return "EmployeeLogin/dashboard";
    }


    @GetMapping("EmployeeLogin/damageReport")
    public String showDamageReport(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("employee", loggedInEmployee);
        return "EmployeeLogin/damageReport";
    }

    @GetMapping("EmployeeLogin/carOverviewEmployee")
    public String showRentedCars(Model model) {
        List<Car> rentedCars = carRepository.findRentedCars();
        model.addAttribute("rentedCars", rentedCars);
        return "EmployeeLogin/carOverviewEmployee";
    }
}