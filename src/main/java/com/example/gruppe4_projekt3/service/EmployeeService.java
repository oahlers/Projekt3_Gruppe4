package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Gemmer en ny medarbejder i databasen efter validering af brugernavn og adgangskode.
    public void saveEmployee(Employee employee) {
        if (employee.getUsername() == null || employee.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Brugernavn må ikke være tomt");
        }
        if (employee.getPassword() == null || employee.getPassword().length() < 8) {
            throw new IllegalArgumentException("Adgangskoden skal være mindst 8 tegn");
        }
        if (employeeRepository.findByUsername(employee.getUsername()) != null) {
            throw new IllegalArgumentException("Brugernavnet eksisterer allerede");
        }
        employeeRepository.save(employee);
    }

    // Opdaterer en eksisterende medarbejder i databasen efter validering af brugernavn og adgangskode.
    public void updateEmployee(Employee employee) {
        if (employee.getUsername() == null || employee.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Brugernavn må ikke være tomt");
        }
        if (employee.getPassword() != null && !employee.getPassword().isEmpty() && employee.getPassword().length() < 8) {
            throw new IllegalArgumentException("Adgangskoden skal være mindst 8 tegn");
        }
        employeeRepository.update(employee);
    }

    // Validerer en medarbejders loginoplysninger ved at tjekke ID, brugernavn og adgangskode.
    public boolean validateLogin(int employeeId, String username, String password) {
        Employee employee = employeeRepository.findByEmployeeIdAndUsername(employeeId, username);
        return employee != null && password.equals(employee.getPassword());
    }

    // Finder en medarbejder ud fra deres ID og returnerer null, hvis de ikke findes.
    public Employee findByEmployeeId(int employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    // Henter en liste over alle medarbejdere i databasen.
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}