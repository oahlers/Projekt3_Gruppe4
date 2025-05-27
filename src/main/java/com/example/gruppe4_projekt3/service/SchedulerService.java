package com.example.gruppe4_projekt3.service;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Rental;
import com.example.gruppe4_projekt3.repository.CarRepository;
import com.example.gruppe4_projekt3.repository.RentalRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SchedulerService {
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final JdbcTemplate jdbcTemplate;

    public SchedulerService(CarRepository carRepository, RentalRepository rentalRepository, JdbcTemplate jdbcTemplate) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Opdaterer bilers skadesstatus dagligt baseret på deres lejeaftalers udløbsdato.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateReadyForUseStatus() {
        List<Rental> activeRentals = rentalRepository.findAllActive();
        for (Rental rental : activeRentals) {
            if (rental.getReadyForUseDate() != null && !rental.getReadyForUseDate().isAfter(LocalDate.now())) {
                carRepository.update(new Car() {{
                    setCarId(rental.getCarId());
                    setNeedsDamageReport(true);
                }});
            }
        }
    }

    // Sporer dagligt bilers tilgængelighed og logger perioder, hvor de bliver utilgængelige.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    @Scheduled(cron = "0 0 0 * * ?")
    public void trackAvailability() {
        List<Car> cars = carRepository.findAll();
        for (Car car : cars) {
            Long carId = car.getCarId();
            boolean isRented = car.isRented();
            int count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM AvailabilityTracking WHERE car_id = ?",
                    Integer.class, carId);

            if (!isRented && count == 0) {
                jdbcTemplate.update(
                        "INSERT INTO AvailabilityTracking (car_id, start_date) VALUES (?, ?)",
                        carId, LocalDate.now());
            } else if (isRented && count > 0) {
                LocalDate startDate = jdbcTemplate.queryForObject(
                        "SELECT start_date FROM AvailabilityTracking WHERE car_id = ?",
                        (rs, rowNum) -> rs.getDate("start_date").toLocalDate(),
                        carId);
                long duration = ChronoUnit.DAYS.between(startDate, LocalDate.now());

                jdbcTemplate.update(
                        "INSERT INTO availability_log (car_id, start_date, end_date, duration_days) VALUES (?, ?, ?, ?)",
                        carId, startDate, LocalDate.now(), duration);

                jdbcTemplate.update(
                        "DELETE FROM AvailabilityTracking WHERE car_id = ?",
                        carId);
            }
        }
    }
}