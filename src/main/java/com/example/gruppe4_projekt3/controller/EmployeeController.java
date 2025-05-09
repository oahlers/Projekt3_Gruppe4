package com.example.gruppe4_projekt3.controller;

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

// EmployeeController. håndterer POST- og GET anmodninger for medarbejder funktioner
// login, register, findEmployee

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CarRepository carRepository;

    // Håndterer login af medarbejder og gemmer session, hvis oplysningerne er korrekte.
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
            return "index";
        }
    }

    // Registrerer en ny medarbejder.
    @PostMapping("/register")
    public String register(@RequestParam("employeeId") int employeeId,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {
        if (password.length() < 8) {
            model.addAttribute("registerError", "Adgangskoden skal være mindst 8 tegn.");
            return "index";
        }
        Employee existingEmployeeById = employeeRepository.findByEmployeeId(employeeId);
        Employee existingEmployeeByUsername = employeeRepository.findByUsername(username);
        if (existingEmployeeById != null) {
            model.addAttribute("registerError", "EmployeeID eksisterer allerede");
            return "index";
        }
        if (existingEmployeeByUsername != null) {
            model.addAttribute("registerError", "Brugernavnet eksisterer allerede");
            return "index";
        }
        Employee newEmployee = new Employee(employeeId, fullName, username, password);
        employeeRepository.save(newEmployee);
        return "index";
    }

    // Søger efter en medarbejder ud fra ID og/eller brugernavn.
    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employeeOverView";
    }
}