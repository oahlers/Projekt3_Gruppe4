package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Rental;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.DamageReportRepository;
import com.example.gruppe4_projekt3.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// DamageReportController. håndterer POST- og GET anmodninger for skaderapport funktioner
// submitDamageReport

@Controller
public class  DamageReportController {

    private final CarRepository carRepository;
    private final DamageReportRepository damageReportRepository;
    private final EmployeeRepository employeeRepository;

    public DamageReportController(CarRepository carRepository,
                                  DamageReportRepository damageReportRepository,
                                  EmployeeRepository employeeRepository) {
        this.carRepository = carRepository;
        this.damageReportRepository = damageReportRepository;
        this.employeeRepository = employeeRepository;
    }


    @PostMapping("/damageReportDone/{id}")
    public String submitDamageReport(@PathVariable("id") Long carId,
                                     @RequestParam(name = "report", required = false) String[] reports,
                                     @RequestParam(name = "price", required = false) double[] prices,
                                     @RequestParam int mileage,
                                     @RequestParam String overallDescription,
                                     HttpSession session,
                                     Model model) {

        if (reports == null) {
            reports = new String[0];
        }
        if (prices == null) {
            prices = new double[0];
        }

        Car car = carRepository.findById(carId);
        Rental rental = damageReportRepository.findLatestRentalByCarId(carId);
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee == null) {
            employee = employeeRepository.findByEmployeeId(1);
        }

        DamageReport damageReport = new DamageReport(car, employee, rental.getCustomerEmail(), mileage);
        damageReport.setReports(reports);
        damageReport.setPrices(prices);
        damageReportRepository.save(damageReport);

        double totalDamagePrice = 0;
        List<Map.Entry<String, Double>> damageList = new ArrayList<>();

        for (int i = 0; i < reports.length; i++) {
            if (reports[i] != null && !reports[i].isBlank()) {
                damageList.add(Map.entry(reports[i], prices[i]));
                totalDamagePrice += prices[i];
            }
        }


        double kmFee = mileage * 0.75;
        double totalPrice = totalDamagePrice + kmFee;

        model.addAttribute("rental", rental);
        model.addAttribute("car", car);
        model.addAttribute("employee", employee);
        model.addAttribute("overallDescription", overallDescription);
        model.addAttribute("mileage", mileage);
        model.addAttribute("damageList", damageList);
        model.addAttribute("kmFee", kmFee);
        model.addAttribute("totalDamagePrice", totalDamagePrice);
        model.addAttribute("totalPrice", totalPrice);

        return "damageReportDone";
    }
    @GetMapping("/damageReportDone/{carId}")
    public String showDamageReport(@PathVariable Long carId, Model model) {
        Car car = carRepository.findById(carId);
        DamageReport report = damageReportRepository.findLatestByCarId(carId);
        Rental rental = damageReportRepository.findLatestRentalByCarId(carId);


        List<Map.Entry<String, Double>> damageList = new ArrayList<>();
        double totalDamagePrice = 0;

        if (report.getReports() != null && report.getPrices() != null) {
            for (int i = 0; i < report.getReports().length; i++) {
                String desc = report.getReports()[i];
                double price = report.getPrices()[i];
                if (desc != null && !desc.isBlank()) {
                    damageList.add(Map.entry(desc, price));
                    totalDamagePrice += price;
                }
            }
        }

        double mileage = report.getMileage();
        double kmFee = mileage * 0.75;
        double totalPrice = totalDamagePrice + kmFee;

        model.addAttribute("car", car);
        model.addAttribute("rental", rental);
        model.addAttribute("employee", report.getEmployee());
        model.addAttribute("mileage", mileage);
        model.addAttribute("overallDescription", "");
        model.addAttribute("damageList", damageList);
        model.addAttribute("kmFee", kmFee);
        model.addAttribute("totalDamagePrice", totalDamagePrice);
        model.addAttribute("totalPrice", totalPrice);

        return "damageReportDone";
    }






}