package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CarRepository carRepository;

    // Viser startsiden.
    @GetMapping("/")
    public String showIndexPage() {
        return "Homepage/index";
    }

    // Viser login- og registreringssiden hvis bruger ikke er logget ind.
    @GetMapping("/auth")
    public String showAuthPage() {
        return "Homepage/index";
    }

    // Viser dashboardet.
    @GetMapping("/EmployeeLogin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("employee", loggedInEmployee);
        return "EmployeeLogin/dashboard";
    }

    // Viser en oversigt over alle biler.
    @GetMapping("/EmployeeLogin/carOverviewEmployee")
    public String showAllCars(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        List<Car> allCars = carRepository.findAll();
        model.addAttribute("allCars", allCars);
        model.addAttribute("employee", loggedInEmployee);

        return "EmployeeLogin/carOverviewEmployee";
    }

    // Viser formularen til søgning efter medarbejder.
    @GetMapping("/EmployeeLogin/searchEmployee")
    public String showSearchForm() {
        return "EmployeeLogin/searchEmployee";
    }

    // Omdirigerer root-URL for /EmployeeLogin til statistiksiden.
    @RequestMapping("/EmployeeLogin/statistics")
    @GetMapping("")
    public String redirectToStatistics() {
        return "redirect:/EmployeeLogin/statistics";
    }
}