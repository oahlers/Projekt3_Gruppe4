package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Damage;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.Rental;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DamageReportRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository; // Tilføjet for at bruge RentalRowMapper

    public DamageReportRepository(JdbcTemplate jdbcTemplate, CarRepository carRepository, RentalRepository rentalRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    // Gemmer en ny skadesrapport i databasen sammen med tilhørende skader og nulstiller bilens status.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public void save(DamageReport damageReport) {
        String insertReportSql = "INSERT INTO damage_report (car_id, mileage, employee_id, customer_email) " +
                "VALUES (?, ?, ?, ?)";
        Integer employeeId = damageReport.getEmployee() != null ? damageReport.getEmployee().getEmployeeId() : null;
        jdbcTemplate.update(insertReportSql,
                damageReport.getCar().getCarId(),
                damageReport.getMileage(),
                employeeId,
                damageReport.getCustomerEmail());

        Long reportId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        String insertDamageSql = "INSERT INTO damage (report_id, description, price) VALUES (?, ?, ?)";
        for (Damage damage : damageReport.getDamages()) {
            if (damage.getDescription() != null && !damage.getDescription().isEmpty()) {
                jdbcTemplate.update(insertDamageSql, reportId, damage.getDescription(), damage.getPrice());
            }
        }

        carRepository.resetAfterDamageReport(damageReport.getCar().getCarId());
    }

    // Henter alle skadesrapporter fra databasen sammen med tilhørende skader.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public List<DamageReport> findAll() {
        String sql = "SELECT dr.*, c.brand, c.model, c.chassis_number, c.license_plate, " +
                "e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id";
        List<DamageReport> reports = jdbcTemplate.query(sql, new DamageReportRowMapper());

        for (DamageReport report : reports) {
            List<Damage> damages = jdbcTemplate.query(
                    "SELECT * FROM damage WHERE report_id = ?",
                    new DamageRowMapper(), report.getReportId());
            report.setDamages(damages);
        }
        return reports;
    }

    // Finder den seneste skadesrapport for en given bil og returnerer null, hvis ingen findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public DamageReport findLatestByCarId(Long carId) {
        String sql = "SELECT dr.*, c.brand, c.model, c.chassis_number, c.license_plate, " +
                "e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id " +
                "WHERE dr.car_id = ? ORDER BY dr.report_id DESC LIMIT 1";
        try {
            DamageReport report = jdbcTemplate.queryForObject(sql, new DamageReportRowMapper(), carId);
            if (report != null) {
                List<Damage> damages = jdbcTemplate.query(
                        "SELECT * FROM damage WHERE report_id = ?",
                        new DamageRowMapper(), report.getReportId());
                report.setDamages(damages);
            }
            return report;
        } catch (Exception e) {
            return null;
        }
    }

    // Finder den seneste aktive lejeaftale for en given bil og returnerer null, hvis ingen findes.
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    public Rental findLatestRentalByCarId(Long carId) {
        String sql = "SELECT * FROM rental WHERE car_id = ? AND end_date IS NULL ORDER BY start_date DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, rentalRepository::mapRental, carId);
        } catch (Exception e) {
            return null;
        }
    }

    // Rowmapper for skadesrapporter
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    private static class DamageReportRowMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            DamageReport report = new DamageReport();
            report.setReportId(rs.getLong("report_id"));

            Car car = new Car();
            car.setCarId(rs.getLong("car_id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setChassisNumber(rs.getString("chassis_number"));
            car.setLicensePlate(rs.getString("license_plate"));
            report.setCar(car);

            Employee employee = new Employee();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullName(rs.getString("employee_fullname"));
            report.setEmployee(employee);

            report.setCustomerEmail(rs.getString("customer_email"));
            report.setMileage(rs.getInt("mileage"));
            return report;
        }
    }

    // Rowmapper for Damage
    // [ Rasmus Guldborg Pedersen ] [ Oliver Ahlers ]
    private static class DamageRowMapper implements RowMapper<Damage> {
        @Override
        public Damage mapRow(ResultSet rs, int rowNum) throws SQLException {
            Damage damage = new Damage();
            damage.setDamageId(rs.getLong("damage_id"));
            damage.setReportId(rs.getLong("report_id"));
            damage.setDescription(rs.getString("description"));
            damage.setPrice(rs.getDouble("price"));
            return damage;
        }
    }
}