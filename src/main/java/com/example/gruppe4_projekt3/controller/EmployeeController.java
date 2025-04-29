package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/login")
    public String login(@RequestParam("employeeId") int employeeId,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        Employee employee = employeeRepository.findByEmployeeIdAndUsername(employeeId, username);
        if (employee != null && employee.getPassword().equals(password)) {
            session.setAttribute("loggedInEmployee", employee);
            model.addAttribute("loggedInEmployee", employee);
            return "redirect:EmployeeLogin/dashboard";
        } else {
            model.addAttribute("loginError", "Ugyldigt EmployeeID, brugernavn eller adgangskode");
            return "Homepage/index";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam("employeeId") int employeeId,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {
        if (password.length() < 8) {
            model.addAttribute("registerError", "Adgangskoden skal være mindst 8 tegn.");
            return "Homepage/index";
        }
        Employee existingEmployeeById = employeeRepository.findByEmployeeId(employeeId);
        Employee existingEmployeeByUsername = employeeRepository.findByUsername(username);
        if (existingEmployeeById != null) {
            model.addAttribute("registerError", "EmployeeID eksisterer allerede");
            return "Homepage/index";
        }
        if (existingEmployeeByUsername != null) {
            model.addAttribute("registerError", "Brugernavnet eksisterer allerede");
            return "Homepage/index";
        }
        Employee newEmployee = new Employee(employeeId, fullName, username, password);
        employeeRepository.save(newEmployee);
        return "HomePage/index";
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

    @GetMapping("/EmployeeLogin/carOverviewEmployee")
    public String showRentedCars(Model model) {
        List<Car> rentedCars = carRepository.findRentedCars();
        model.addAttribute("rentedCars", rentedCars);
        return "/EmployeeLogin/carOverviewEmployee";
    }
    @GetMapping("/find")
    public String findEmployee(
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) String username,
            Model model
    ) {
        Employee employee = null;

        if (employeeId != null && username != null) {
            employee = employeeRepository.findByEmployeeIdAndUsername(employeeId, username);
        } else if (employeeId != null) {
            employee = employeeRepository.findByEmployeeId(employeeId);
        } else if (username != null) {
            employee = employeeRepository.findByUsername(username);
        }

        if (employee != null) {
            model.addAttribute("employee", employee);
            return "employeeDetails"; // fx en Thymeleaf-side: employeeDetails.html
        } else {
            model.addAttribute("message", "Medarbejder blev ikke fundet.");
            return "employeeNotFound"; // fx employeeNotFound.html
        }
    }
}