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

    // Viser startsiden.
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    // Viser login- og registreringssiden hvis bruger ikke er logget ind.
    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    // Viser dashboardet.
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("employee", loggedInEmployee);
        return "dashboard";
    }

    // Viser en oversigt over alle biler.
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

    // Viser leveringssiden med liste over tilgængelige biler.
    @GetMapping("/registerAndDeliverCar")
    public String showDeliverCarPage(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("availableCars", carRepository.findAvailableForLoan());
        return "registerAndDeliverCar";
    }

    // Viser statistik over gennemsnitlig betalingstid, transporttid og lejeperiode.
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

    // Viser en liste over biler der mangler skadesrapport.
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

    // Viser siden for at udfylde en skadesrapport for en bestemt bil.
    @GetMapping("/damageReportFill/{id}")
    public String showFillReportPage(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id);
        if (car == null || !car.isReadyForUse()) {
            return "error";
        }
        model.addAttribute("car", car);
        return "damageReportConfirmation";
    }

    // Viser bekræftelsessiden efter indsendelse af skadesrapport.
    @GetMapping("/damageReportDone")
    public String showDamageReportDone() {
        return "damageReportDone";
    }

    // Viser historikken over alle tidligere skadesrapporter.
    @GetMapping("/damageReportHistory")
    public String showDamageReportHistory(Model model) {
        List<DamageReport> damageReports = damageReportRepository.findAll();
        model.addAttribute("damageReports", damageReports);
        return "damageReportHistory";
    }

    // Viser formularen til søgning efter medarbejder.
    // For almindelige medarbejdere
    @GetMapping("/employeeOverview")
    public String getAllEmployees(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth"; // Hvis ikke logget ind, omdiriger til login-siden
        }

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);

        // Vis employeeOverview for almindelige brugere
        return "employeeOverview";
    }

    // For administratorer
    @GetMapping("/employeeOverviewAdmin")
    public String getAllEmployeesAdmin(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null || !isAdmin(session)) {
            return "redirect:/auth"; // Hvis ikke logget ind eller ikke admin, omdiriger til login-siden
        }

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);

        // Vis employeeOverviewAdmin for administratorer
        return "employeeOverviewAdmin";
    }

    // Helper metode til at tjekke om brugeren er admin
    private boolean isAdmin(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        return employee != null && "ADMIN".equalsIgnoreCase(employee.getRole());
    }


    // Viser alle biler og formular til at tilføje ny bil.
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