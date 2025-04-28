package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Behandler loginformularen, validerer employeeId, brugernavn og adgangskode, og opretter session for medarbejderen
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
            return "redirect:/dashboard";
        } else {
            model.addAttribute("loginError", "Ugyldigt EmployeeID, brugernavn eller adgangskode");
            return "login";
        }
    }

    // Behandler registreringsformularen, opretter en ny medarbejder i databasen
    @PostMapping("/register")
    public String register(@RequestParam("employeeId") int employeeId,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {
        if (password.length() < 8) {
            model.addAttribute("registerError", "Adgangskoden skal være mindst 8 tegn.");
            return "login";
        }
        Employee existingEmployeeById = employeeRepository.findByEmployeeId(employeeId);
        Employee existingEmployeeByUsername = employeeRepository.findByUsername(username);
        if (existingEmployeeById != null) {
            model.addAttribute("registerError", "EmployeeID eksisterer allerede");
            return "login";
        }
        if (existingEmployeeByUsername != null) {
            model.addAttribute("registerError", "Brugernavnet eksisterer allerede");
            return "login";
        }
        Employee newEmployee = new Employee(employeeId, fullName, username, password);
        employeeRepository.save(newEmployee);
        return "redirect:/auth";
    }

    // Tilbage til index efter oprettelse af bruger
    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    //Omdirigering til dashboard efter login
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth"; // Omdiriger til login, hvis ikke logget ind
        }
        model.addAttribute("employee", loggedInEmployee);
        return "dashboard";
    }
}