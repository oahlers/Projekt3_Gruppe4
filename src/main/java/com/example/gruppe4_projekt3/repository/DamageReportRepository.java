package com.example.gruppe4_projekt3.repository;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.DamageReport;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.model.Rental;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class  DamageReportRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CarRepository carRepository;

    public DamageReportRepository(JdbcTemplate jdbcTemplate, CarRepository carRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.carRepository = carRepository;
    }

    public void save(DamageReport damageReport) {
        String insertHeaderSql = "INSERT INTO damage_report (car_id, mileage, employee_id, customer_email) " +
                "VALUES (?, ?, ?, ?)";

        Integer employeeId = damageReport.getEmployee() != null ? damageReport.getEmployee().getEmployeeId() : null;

        jdbcTemplate.update(insertHeaderSql,
                damageReport.getCar().getCarId(),
                damageReport.getMileage(),
                employeeId,
                damageReport.getCustomerEmail());

        Long reportId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        jdbcTemplate.update("UPDATE damage_report SET report_group_id = ? WHERE report_id = ?",
                reportId, reportId);

        String insertDamageSql = "INSERT INTO damage_report (car_id, mileage, employee_id, customer_email, " +
                "report, price, report_group_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        for (int i = 0; i < damageReport.getReports().length; i++) {
            if (damageReport.getReports()[i] != null && !damageReport.getReports()[i].isEmpty()) {
                jdbcTemplate.update(insertDamageSql,
                        damageReport.getCar().getCarId(),
                        damageReport.getMileage(),
                        employeeId,
                        damageReport.getCustomerEmail(),
                        damageReport.getReports()[i],
                        damageReport.getPrices()[i],
                        reportId
                );
            }
        }

        carRepository.resetAfterDamageReport(damageReport.getCar().getCarId());
    }


    public List<DamageReport> findAll() {
        String groupIdSql = "SELECT report_group_id FROM damage_report " +
                "WHERE report_id = report_group_id " +
                "ORDER BY report_id DESC";

        List<Long> reportGroupIds = jdbcTemplate.query(groupIdSql, (rs, rowNum) -> rs.getLong("report_group_id"));

        List<DamageReport> damageReports = new ArrayList<>();

        String sql = "SELECT dr.*, c.brand, c.model, c.chassis_number, c.license_plate, " +
                "e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id " +
                "WHERE dr.report_group_id = ? " +
                "ORDER BY dr.report_id";

        for (Long reportGroupId : reportGroupIds) {
            List<DamageReport> allRecords = jdbcTemplate.query(sql, new DamageReportRowMapper(), reportGroupId);

            if (allRecords.isEmpty()) {
                continue;
            }

            DamageReport mainReport = allRecords.get(0);

            List<String> reports = new ArrayList<>();
            List<Double> prices = new ArrayList<>();

            for (DamageReport record : allRecords) {
                if (record.getReports() != null && record.getReports().length > 0 &&
                        record.getReports()[0] != null && !record.getReports()[0].isEmpty()) {
                    reports.add(record.getReports()[0]);
                    prices.add(record.getPrices()[0]);
                }
            }

            mainReport.setReports(reports.toArray(new String[0]));
            mainReport.setPrices(prices.stream().mapToDouble(Double::doubleValue).toArray());
            mainReport.setReportId(reportGroupId);

            damageReports.add(mainReport);
        }

        return damageReports;
    }

    public DamageReport findLatestByCarId(Long carId) {
        String groupIdSql = "SELECT report_group_id FROM damage_report " +
                "WHERE car_id = ? AND report_id = report_group_id " +
                "ORDER BY report_id DESC LIMIT 1";

        Long reportGroupId;
        try {
            reportGroupId = jdbcTemplate.queryForObject(groupIdSql, Long.class, carId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        if (reportGroupId == null) {
            return null;
        }

        String sql = "SELECT dr.*, c.brand, c.model, c.chassis_number, c.license_plate, " +
                "e.fullname AS employee_fullname " +
                "FROM damage_report dr " +
                "JOIN car c ON dr.car_id = c.car_id " +
                "JOIN employees e ON dr.employee_id = e.employee_id " +
                "WHERE dr.report_group_id = ? " +
                "ORDER BY dr.report_id";

        List<DamageReport> allRecords = jdbcTemplate.query(sql, new DamageReportRowMapper(), reportGroupId);

        if (allRecords.isEmpty()) {
            return null;
        }

        DamageReport mainReport = allRecords.get(0);

        List<String> reports = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        for (DamageReport record : allRecords) {
            if (record.getReports() != null && record.getReports().length > 0 &&
                    record.getReports()[0] != null && !record.getReports()[0].isEmpty()) {
                reports.add(record.getReports()[0]);
                prices.add(record.getPrices()[0]);
            }
        }

        mainReport.setReports(reports.toArray(new String[0]));
        mainReport.setPrices(prices.stream().mapToDouble(Double::doubleValue).toArray());
        mainReport.setReportId(reportGroupId);

        return mainReport;
    }

    public List<Object[]> findDamagesByCarId(Long carId) {
        String sql = "SELECT report, price FROM damage_report WHERE car_id = ? ORDER BY report_id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new Object[]{rs.getString("report"), rs.getDouble("price")},
                carId);
    }

    public Rental findLatestRentalByCarId(Long carId) {
        String sql = "SELECT r.rental_id, r.car_id, r.customer_name, r.customer_email, r.delivery_address, " +
                "r.rental_months, r.ready_for_use_date, r.payment_time, r.transport_time, " +
                "r.subscription_type_id, r.mileage " +
                "FROM rental r " +
                "JOIN car c ON r.car_id = c.car_id " +
                "WHERE r.car_id = ? " +
                "ORDER BY r.start_date DESC " +
                "LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new RentalRowMapper(), carId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class DamageReportRowMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            DamageReport report = new DamageReport();

            Car car = new Car();
            car.setCarId(rs.getLong("car_id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setChassisNumber(rs.getString("chassis_number"));
            car.setLicensePlate(rs.getString("license_plate"));

            Employee employee = new Employee();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullName(rs.getString("employee_fullname"));

            report.setCar(car);
            report.setEmployee(employee);
            report.setCustomerEmail(rs.getString("customer_email"));
            report.setMileage(rs.getInt("mileage"));

            String[] reports = new String[1];
            reports[0] = rs.getString("report");
            report.setReports(reports);

            double[] prices = new double[1];
            prices[0] = rs.getDouble("price");
            report.setPrices(prices);

            return report;
        }
    }


    private static class RentalRowMapper implements RowMapper<Rental> {
        @Override
        public Rental mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rental rental = new Rental();
            rental.setRentalId(rs.getLong("rental_id"));
            rental.setCarId(rs.getLong("car_id"));
            rental.setCustomerName(rs.getString("customer_name"));
            rental.setCustomerEmail(rs.getString("customer_email"));
            rental.setDeliveryAddress(rs.getString("delivery_address"));
            rental.setRentalMonths(rs.getInt("rental_months"));
            rental.setMileage(rs.getInt("mileage"));

            try {
                rental.setSubscriptionType(rs.getString("subscription_type_id"));
            } catch (SQLException e) {
                rental.setSubscriptionType(null);
            }

            return rental;
        }
    }
}
