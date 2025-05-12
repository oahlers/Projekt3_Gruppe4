package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.DamageReportRepository;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Pagecontroller, står for håndtering af alle sidevisninger (GET-Anmodninger til visning af en side)

// Generelle sider
// showIndexPage, showAuthPage, showDashboard

// Bil-relateret sider
// showAllCars, viewAllCars, showDeliverCarPage

// Skaderapport-sider
// showDamageReportList, showFillReportPage, showDamageReportDone, showDamageReportHistory

// Diverse sider (Medarbejdersøgning og statistik)
// showSearchForm, showStatistics

@Controller
public class PageController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DamageReportRepository damageReportRepository;

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("employee", loggedInEmployee);
        return "dashboard";
    }

    @GetMapping("/carOverview")
    public String showAllCars(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        List<Car> allCars = carRepository.findAll();
        model.addAttribute("allCars", allCars);
        model.addAttribute("employee", loggedInEmployee);

        return "carOverview";
    }

    @GetMapping("/cars/edit/{id}")
    public String editCar(@PathVariable Long id, Model model, HttpSession session) {
        Car car = carRepository.findById(id);
        if (car == null) {
            return "error";
        }
        model.addAttribute("car", car);
        return "carOverviewEdit";
    }

    @GetMapping("/cars/details/{id}")
    public String showCarOverviewDetails(@PathVariable Long id, Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        Car car = carRepository.findById(id);
        if (car == null) {
            return "error";
        }

        model.addAttribute("car", car);
        return "carOverviewDetails";
    }


    @GetMapping("/registerAndDeliverCar")
    public String showDeliverCarPage(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("availableCars", carRepository.findAvailableForLoan());
        return "registerAndDeliverCar";
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        double averagePaymentTime = carRepository.getAveragePaymentTime();
        double averageTransportTime = carRepository.getAverageTransportTime();
        double averageRentalDuration = carRepository.getAverageRentalDurationPerCar();
        model.addAttribute("averagePaymentTime", carRepository.getAveragePaymentTime());
        model.addAttribute("averageTransportTime", carRepository.getAverageTransportTime());
        model.addAttribute("averageRentalDurationPerCar", carRepository.getAverageRentalDurationPerCar());
        return "statistics";
    }

    @GetMapping("/damageReport")
    public String showDamageReportList(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Car> cars = carRepository.findCarsNeedingDamageReport();
        model.addAttribute("employee", loggedInEmployee);
        model.addAttribute("cars", cars);
        return "damageReport";
    }

    @GetMapping("/damageReportFill/{id}")
    public String showFillReportPage(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id);
        if (car == null || !car.isReadyForUse()) {
            return "error";
        }
        model.addAttribute("car", car);
        return "damageReportConfirmation";
    }

    @GetMapping("/damageReportDone")
    public String showDamageReportDone() {
        return "damageReportDone";
    }

    @GetMapping("/damageReportHistory")
    public String showDamageReportHistory(Model model) {
        List<DamageReport> damageReports = damageReportRepository.findAll();
        model.addAttribute("damageReports", damageReports);
        return "damageReportHistory";
    }

    @GetMapping("/employeeOverview")
    public String getAllEmployees(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);

        return "employeeOverview";
    }

    @GetMapping("/employeeOverviewAdmin")
    public String getAllEmployeesAdmin(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null || !isAdmin(session)) {
            return "redirect:/auth";
        }

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);

        return "employeeOverviewAdmin";
    }

    private boolean isAdmin(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        return employee != null && "ADMIN".equalsIgnoreCase(employee.getRole());
    }

    @GetMapping("/addCars")
    public String viewAllCars(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/";
        }
        if ("ADMIN".equals(employee.getRole())) {
        }
        return "addCars";
    }
}
