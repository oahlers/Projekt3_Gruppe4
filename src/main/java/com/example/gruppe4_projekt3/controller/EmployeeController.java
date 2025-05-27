package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Håndterer login for en medarbejder og redirecter til dashboardet, hvis det lykkes, eller viser en fejl.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @PostMapping("/login")
    public String login(@RequestParam("employeeId") int employeeId,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try {
            if (employeeService.validateLogin(employeeId, username, password)) {
                Employee employee = employeeService.findByEmployeeId(employeeId);
                session.setAttribute("loggedInEmployee", employee);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("loginError", "Ugyldigt EmployeeID, brugernavn eller adgangskode");
                return "index";
            }
        } catch (Exception e) {
            model.addAttribute("loginError", "En fejl opstod under login. Prøv igen.");
            return "index";
        }
    }

    // Opretter en ny medarbejder, hvis skaberen er admin, og redirecter til medarbejderoversigten.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @PostMapping("/employeeOverviewAdmin")
    public String register(@RequestParam("fullName") String fullName,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("role") String role,
                           Model model,
                           HttpSession session) {
        if (!isAdmin(session)) {
            model.addAttribute("registerError", "Du har ikke tilladelse til at oprette en medarbejder");
            return "employeeOverviewAdmin";
        }

        try {
            Employee newEmployee = new Employee();
            newEmployee.setFullName(fullName);
            newEmployee.setUsername(username);
            newEmployee.setPassword(password);
            newEmployee.setRole(role);
            employeeService.saveEmployee(newEmployee);
            return "redirect:/employeeOverviewAdmin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registerError", e.getMessage());
            return "employeeOverviewAdmin";
        }
    }

    // Viser en oversigt over alle medarbejdere for en admin-bruger og redirecter til login, hvis brugeren ikke er logget ind.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/employeeOverviewAdmin")
    public String getAllEmployees(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null || !isAdmin(session)) {
            return "redirect:/auth";
        }
        model.addAttribute("employees", employeeService.findAll());
        return "employeeOverviewAdmin";
    }

    // Viser formularen til redigering af en medarbejder for en admin-bruger og redirecter til oversigten, hvis medarbejderen ikke findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/employee/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null || !isAdmin(session)) {
            return "redirect:/auth";
        }
        Employee employee = employeeService.findByEmployeeId(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "employeeEdit";
        }
        return "redirect:/employeeOverviewAdmin";
    }

    // Opdaterer en eksisterende medarbejder og redirecter til medarbejderoversigten, eller viser en fejl.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @PostMapping("/employee/edit/{id}")
    public String updateEmployee(@PathVariable int id, @ModelAttribute Employee employee, Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null || !isAdmin(session)) {
            return "redirect:/auth";
        }
        try {
            employee.setEmployeeId(id);
            employeeService.updateEmployee(employee);
            return "redirect:/employeeOverviewAdmin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("editError", e.getMessage());
            model.addAttribute("employee", employee);
            return "employeeEdit";
        }
    }

    private boolean isAdmin(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        return employee != null && "ADMIN".equalsIgnoreCase(employee.getRole());
    }

    // Viser detaljer for en specifik medarbejder og viser en fejl, hvis medarbejderen ikke findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/employee/details/{id}")
    public String showEmployeeDetails(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        Employee employee = employeeService.findByEmployeeId(id);
        if (employee == null) {
            model.addAttribute("errorMessage", "Medarbejder ikke fundet.");
            return "error";
        }
        model.addAttribute("employee", employee);
        return "employeeDetails";
    }
}