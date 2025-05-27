package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Damage;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.DamageReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageReportService {
    private final DamageReportRepository damageReportRepository;
    private final CarRepository carRepository;

    public DamageReportService(DamageReportRepository damageReportRepository, CarRepository carRepository) {
        this.damageReportRepository = damageReportRepository;
        this.carRepository = carRepository;
    }

    // Opretter en ny skadesrapport for en bil og gemmer den i databasen efter validering.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void createDamageReport(Long carId, Employee employee, String customerEmail, int mileage, List<Damage> damages) {
        Car car = carRepository.findById(carId);
        if (car == null || !car.isNeedsDamageReport()) {
            throw new IllegalStateException("Bilen er ikke klar til skaderapport");
        }
        DamageReport report = new DamageReport();
        report.setCar(car);
        report.setEmployee(employee);
        report.setCustomerEmail(customerEmail);
        report.setMileage(mileage);
        report.setDamages(damages);
        damageReportRepository.save(report);
    }

    // Finder den seneste skadesrapport for en given bil og returnerer null, hvis ingen findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public DamageReport findLatestByCarId(Long carId) {
        return damageReportRepository.findLatestByCarId(carId);
    }

    // Henter alle skadesrapporter fra databasen.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<DamageReport> findAll() {
        return damageReportRepository.findAll();
    }

    // Beregner den samlede pris for en skadesrapport inklusive kilometergebyr.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public double calculateTotalDamagePrice(List<Damage> damages, int mileage) {
        double totalDamagePrice = damages.stream().mapToDouble(Damage::getPrice).sum();
        double kmFee = mileage * 0.75;
        return totalDamagePrice + kmFee;
    }
}