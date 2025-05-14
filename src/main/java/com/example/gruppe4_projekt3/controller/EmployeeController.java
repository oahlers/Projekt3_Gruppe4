package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                        @RequestParam(value = "role", required = false) String role,
                        HttpSession session,
                        Model model) {
        Employee employee = employeeRepository.findByEmployeeIdAndUsername(employeeId, username);
        if (employee != null && employee.getPassword().equals(password)) {
            session.setAttribute("loggedInEmployee", employee);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("loginError", "Ugyldigt EmployeeID, brugernavn eller adgangskode");
            return "index";
        }
    }

    // Registrerer en ny medarbejder.
    @PostMapping("/employeeOverviewAdmin")
    public String register(@RequestParam("fullName") String fullName,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("role") String role,
                           Model model, HttpSession session) {

        // Tjek om brugeren er administrator
        if (!isAdmin(session)) {
            model.addAttribute("registerError", "Du har ikke tilladelse til at oprette en medarbejder.");
            return "employeeOverView"; // Eller den side, du ønsker at sende brugeren til
        }

        if (password.length() < 8) {
            model.addAttribute("registerError", "Adgangskoden skal være mindst 8 tegn.");
            return "employeeOverView";
        }

        Employee existingEmployeeByUsername = employeeRepository.findByUsername(username);
        if (existingEmployeeByUsername != null) {
            model.addAttribute("registerError", "Brugernavnet eksisterer allerede");
            return "employeeOverView";
        }

        // Opret en ny Employee uden at bruge employeeId
        Employee newEmployee = new Employee();
        newEmployee.setFullName(fullName);
        newEmployee.setUsername(username);
        newEmployee.setPassword(password);
        newEmployee.setRole(role);

        // Gem den nye medarbejder i databasen
        employeeRepository.save(newEmployee);

        return "redirect:/employeeOverviewAdmin"; // Redirect for at vise den opdaterede liste
    }

    // Søger efter en medarbejder ud fra ID og/eller brugernavn.
    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employeeOverView";
    }

    private boolean isAdmin(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        return employee != null && "ADMIN".equalsIgnoreCase(employee.getRole());
    }



    @GetMapping("/employee/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "employeeEdit";
        }
        return "redirect:/employee/list";
    }

    @PostMapping("/employee/edit/{id}")
    public String updateEmployee(@PathVariable int id, @ModelAttribute Employee employee) {
        employee.setEmployeeId(id);
        employeeRepository.update(employee);
        return "redirect:/employeeOverviewAdmin";
    }


}