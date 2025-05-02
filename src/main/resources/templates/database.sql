DROP DATABASE IF EXISTS bilabonnement;
CREATE DATABASE bilabonnement;
USE bilabonnement;

DROP DATABASE IF EXISTS bilabonnement;
CREATE DATABASE bilabonnement;
USE bilabonnement;

CREATE TABLE car (
                     car_id INT AUTO_INCREMENT PRIMARY KEY,
                     car_emission INT NOT NULL,
                     year INT NOT NULL,
                     brand VARCHAR(100) NOT NULL,
                     model VARCHAR(100) NOT NULL,
                     color VARCHAR(100) NOT NULL,
                     equipment_level VARCHAR(100) NOT NULL,
                     vehicle_number VARCHAR(100) NOT NULL UNIQUE,
                     chassis_number VARCHAR(100) NOT NULL UNIQUE,
                     price DECIMAL(10,2) NOT NULL,
                     registration_fee DECIMAL(10,2) NOT NULL,
                     isAvailableForLoan BIT NOT NULL,
                     isReadyForUse BIT NOT NULL,
                     payment_time INT NOT NULL
);

INSERT INTO car (car_emission, year, brand, model, color, equipment_level, vehicle_number, chassis_number, price, registration_fee, isAvailableForLoan, isReadyForUse, payment_time)
VALUES
    (0, 2024, 'NAVOR', 'E5 ROCK 218 HK', 'Sort', 'Premium', 'EVN12345', 'NAVORE5ROCK12345678', 329995.00, 18420.00, b'0', b'0', 7),
    (0, 2023, 'Fiat', '500e Icon Pack 118 HK', 'Hvid', 'Icon Pack', 'EVF50001', 'FIAT500EICON12345678', 259995.00, 16230.00, b'0', b'0', 5),
    (100, 2023, 'Tesla', 'Model Y Long Range', 'Blå', 'Standard', 'EVT10001', 'TESLAMODELYLONG12345678', 689995.00, 29550.00, b'1', b'0', 8),
    (50, 2023, 'BMW', 'iX3', 'Grå', 'Xdrive', 'EVB50001', 'BMWIX3123456789', 799995.00, 41250.00, b'1', b'1', 10),
    (0, 2022, 'Nissan', 'Leaf 40 kWh', 'Rød', 'N-Connecta', 'EVN20001', 'NISSANLEAF12345678', 219995.00, 11850.00, b'0', b'1', 6),
    (0, 2021, 'Renault', 'Zoe 52 kWh', 'Grøn', 'Intens', 'EVR50001', 'RENAULTZOE12345678', 189995.00, 8200.00, b'1', b'1', 5),
    (0, 2023, 'Audi', 'Q4 e-tron', 'Blå', 'S Line', 'EVA40001', 'AUDIQ4ETRON12345678', 619995.00, 23400.00, b'0', b'1', 7),
    (0, 2024, 'Volkswagen', 'ID.4', 'Sort', 'Pro Performance', 'EVW20001', 'VOLKSWAGENID4123456789', 479995.00, 20750.00, b'0', b'0', 6);

CREATE TABLE employees (
                           employee_id INT AUTO_INCREMENT PRIMARY KEY,
                           fullname VARCHAR(100) NOT NULL,
                           username VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL
);

INSERT INTO employees (employee_id, username, password, fullname)
VALUES ('1', 'demo', 'demo', 'Demo demosen');

CREATE TABLE damage_report (
                               report_id INT AUTO_INCREMENT PRIMARY KEY,
                               car_id INT NOT NULL,
                               price DECIMAL(10,2) NOT NULL,
                               employee_id INT NOT NULL,
                               customer_email VARCHAR(100),
                               report TEXT,
                               FOREIGN KEY (car_id) REFERENCES car(car_id),
                               FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

