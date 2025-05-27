package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.*;
import com.example.gruppe4_projekt3.service.CarService;
import com.example.gruppe4_projekt3.service.DamageReportService;
import com.example.gruppe4_projekt3.service.EmployeeService;
import com.example.gruppe4_projekt3.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class PageController {
    private final EmployeeService employeeService;
    private final CarService carService;
    private final DamageReportService damageReportService;
    private final RentalService rentalService;

    public PageController(EmployeeService employeeService, CarService carService,
                          DamageReportService damageReportService, RentalService rentalService) {
        this.employeeService = employeeService;
        this.carService = carService;
        this.damageReportService = damageReportService;
        this.rentalService = rentalService;
    }

    // Viser startsiden (index) for applikationen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    // Viser login-siden (auth), som også bruger index-skabelonen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    // Viser dashboardet for en logget ind medarbejder og redirecter til login, hvis brugeren ikke er logget ind.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("employee", loggedInEmployee);
        return "dashboard";
    }

    // Viser en oversigt over alle biler med deres lejeoplysninger og resterende lejeperiode for loggede brugere.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/carOverview")
    public String showAllCars(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Car> allCars = carService.findAllCars();
        List<Rental> activeRentals = rentalService.findAllActive();

        Map<Long, Rental> activeRentalsMap = new HashMap<>();
        Map<Long, Long> remainingDaysMap = new HashMap<>();
        Map<Long, Long> remainingMonthsMap = new HashMap<>();

        LocalDate today = LocalDate.now();

        activeRentals = activeRentals.stream()
                .filter(rental -> rental.getStartDate() != null && rental.getRentalMonths() > 0)
                .filter(rental -> {
                    LocalDate endDate = rental.getStartDate().plusMonths(rental.getRentalMonths());
                    return endDate.isAfter(today);
                })
                .collect(Collectors.toList());

        for (Rental rental : activeRentals) {
            activeRentalsMap.put(rental.getCarId(), rental);
            LocalDate endDate = rental.getStartDate().plusMonths(rental.getRentalMonths());
            long remainingDays = ChronoUnit.DAYS.between(today, endDate);
            long remainingMonths = ChronoUnit.MONTHS.between(today, endDate);
            remainingDaysMap.put(rental.getCarId(), remainingDays);
            remainingMonthsMap.put(rental.getCarId(), remainingMonths);
        }

        double totalPriceAllCars = allCars.stream()
                .mapToDouble(Car::getPrice)
                .sum();

        double totalPriceRentedCars = allCars.stream()
                .filter(car -> activeRentalsMap.containsKey(car.getCarId()))
                .mapToDouble(Car::getPrice)
                .sum();

        model.addAttribute("allCars", allCars);
        model.addAttribute("activeRentalsMap", activeRentalsMap);
        model.addAttribute("remainingDaysMap", remainingDaysMap);
        model.addAttribute("remainingMonthsMap", remainingMonthsMap);
        model.addAttribute("totalPriceAllCars", totalPriceAllCars);
        model.addAttribute("totalPriceRentedCars", totalPriceRentedCars);
        model.addAttribute("employee", loggedInEmployee);
        return "carOverview";
    }

    // Viser formularen til redigering af en bil og viser en fejl, hvis bilen ikke findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/cars/edit/{id}")
    public String editCar(@PathVariable Long id, Model model, HttpSession session) {
        Car car = carService.findCarById(id);
        if (car == null) {
            return "error";
        }
        model.addAttribute("car", car);
        return "carOverviewEdit";
    }

    // Viser detaljer for en specifik bil, inklusive lejeoplysninger og resterende lejedage.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/cars/details/{id}")
    public String showCarDetails(@PathVariable("id") Long carId, Model model) {
        Car car = carService.findCarById(carId);
        Rental rental = rentalService.findLatestRentalByCarId(carId);
        Long remainingDays = null;

        if (rental != null && rental.getStartDate() != null && rental.getRentalMonths() > 0) {
            LocalDate today = LocalDate.now();
            LocalDate endDate = rental.getStartDate().plusMonths(rental.getRentalMonths());
            remainingDays = ChronoUnit.DAYS.between(today, endDate);
        }

        model.addAttribute("car", car);
        model.addAttribute("rental", rental);
        model.addAttribute("remainingDays", remainingDays);
        return "carOverviewDetails";
    }

    // Viser siden til registrering og levering af en bil med en liste over tilgængelige biler.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/registerAndDeliverCar")
    public String showDeliverCarPage(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        model.addAttribute("availableCars", carService.findAvailableForLoan());
        return "registerAndDeliverCar";
    }

    // Viser statistiksiden med gennemsnitlige værdier for betalingstid, transporttid og lejevarighed for en bil.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        Long carId = 1L;
        Double averageRentalDuration = carService.getAverageRentalDurationPerCar(carId);
        model.addAttribute("averagePaymentTime", rentalService.getAveragePaymentTime() != null ? rentalService.getAveragePaymentTime() : 0.0);
        model.addAttribute("averageTransportTime", rentalService.getAverageTransportTime() != null ? rentalService.getAverageTransportTime() : 0.0);
        model.addAttribute("averageRentalDurationPerCar", averageRentalDuration != null ? averageRentalDuration : 0.0);
        return "statistics";
    }

    // Viser en liste over alle biler med deres gennemsnitlige tilgængelighed og lejevarighed.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/statisticsCarList")
    public String getStatisticsCarList(Model model) {
        List<Car> cars = carService.findAllCars();
        for (Car car : cars) {
            Double averageAvailabilityDays = carService.getAverageAvailabilityPerCar(car.getCarId());
            car.setAverageAvailabilityDays(averageAvailabilityDays != null ? averageAvailabilityDays.intValue() : 0);
            Double averageRentalDuration = carService.getAverageRentalDurationPerCar(car.getCarId());
            car.setAverageRentalDuration(averageRentalDuration != null ? averageRentalDuration.intValue() : 0);
        }
        model.addAttribute("cars", cars);
        return "statisticsCarList";
    }

    // Viser en liste over biler, der kræver en skadesrapport, for loggede brugere.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/damageReport")
    public String showDamageReportList(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Car> cars = carService.findCarsNeedingDamageReport();
        List<Rental> activeRentals = rentalService.findAllActive();
        Map<Long, Rental> activeRentalsMap = new HashMap<>();
        Set<Long> limitedCarsReadyForPurchase = new HashSet<>();

        LocalDate today = LocalDate.now();

        for (Rental rental : activeRentals) {
            activeRentalsMap.put(rental.getCarId(), rental);
            if (rental.getSubscriptionTypeId() == 2 && rental.getReadyForUseDate() != null &&
                    !rental.getReadyForUseDate().isAfter(today) && !rental.isPurchased()) {
                limitedCarsReadyForPurchase.add(rental.getCarId());
            }
        }

        model.addAttribute("employee", loggedInEmployee);
        model.addAttribute("cars", cars);
        model.addAttribute("activeRentalsMap", activeRentalsMap);
        model.addAttribute("limitedCarsReadyForPurchase", limitedCarsReadyForPurchase);
        return "damageReport";
    }

    // Viser historikken over alle skadesrapporter i systemet.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/damageReportHistory")
    public String showDamageReportHistory(Model model) {
        List<DamageReport> damageReports = damageReportService.findAll();
        model.addAttribute("damageReports", damageReports);
        return "damageReportHistory";
    }

    // Viser en oversigt over alle medarbejdere for loggede brugere.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/employeeOverview")
    public String getAllEmployees(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "employeeOverview";
    }

    // Viser siden til tilføjelse af en ny bil for loggede brugere.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/addCars")
    public String viewAllCars(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/";
        }
        return "addCars";
    }

    // Viser leveringskalenderen med alle aktive lejeaftaler og deres leveringsdage for loggede brugere.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/carDeliveryCalendar")
    public String showCarDeliveryCalendar(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        List<Rental> rentedCars = rentalService.findAllActive();
        Map<Long, Long> daysUntilDeliveryMap = new HashMap<>();
        LocalDate today = LocalDate.now();

        rentedCars = rentedCars.stream()
                .filter(rental -> rental.getStartDate() != null && rental.getTransportTime() != null)
                .filter(rental -> {
                    LocalDate deliveryDate = rental.getStartDate().plusDays(rental.getTransportTime());
                    return deliveryDate.isAfter(today);
                })
                .collect(Collectors.toList());

        for (Rental rental : rentedCars) {
            LocalDate deliveryDate = rental.getStartDate().plusDays(rental.getTransportTime());
            long daysUntilDelivery = ChronoUnit.DAYS.between(today, deliveryDate);
            daysUntilDeliveryMap.put(rental.getRentalId(), daysUntilDelivery);
        }

        model.addAttribute("rentedCars", rentedCars);
        model.addAttribute("daysUntilDeliveryMap", daysUntilDeliveryMap);
        model.addAttribute("employee", loggedInEmployee);
        return "carDeliveryCalendar";
    }

    // Viser formularen til udfyldelse af en skadesrapport for en bil, hvis den er klar til rapport.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @GetMapping("/damageReportFill/{id}")
    public String showDamageReportForm(@PathVariable Long id, Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/auth";
        }
        Car car = carService.findCarById(id);
        if (car == null || !car.isNeedsDamageReport()) {
            model.addAttribute("errorMessage", "Bilen er ikke klar til skaderapport.");
            return "error";
        }
        Rental rental = rentalService.findLatestRentalByCarId(id);
        model.addAttribute("car", car);
        model.addAttribute("rental", rental);
        return "damageReportConfirmation";
    }
}