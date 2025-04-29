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
                     return_address VARCHAR(100) NOT NULL,
                     vehicle_number VARCHAR(100) NOT NULL UNIQUE,
                     chassis_number VARCHAR(100) NOT NULL UNIQUE,
                     price DECIMAL(10,2) NOT NULL,
                     registration_fee DECIMAL(10,2) NOT NULL,
                     is_car_available BIT NOT NULL,
                     ready_for_loan BIT NOT NULL,
                     payment_time INT NOT NULL,
                     transport_time INT NOT NULL
);

INSERT INTO car (car_emission, year, brand, model, color, equipment_level, return_address, vehicle_number, chassis_number, price, registration_fee, is_car_available, ready_for_loan, payment_time, transport_time)
VALUES
    (0, 2024, 'NAVOR', 'E5 ROCK 218 HK', 'Sort', 'Premium', 'Elbilvej 10, 2100 København Ø', 'EVN12345', 'NAVORE5ROCK12345678', 329995.00, 18420.00, b'0', b'0', 7, 2),
    (0, 2023, 'Fiat', '500e Icon Pack 118 HK', 'Hvid', 'Icon Pack', 'Elbilvej 10, 2100 København Ø', 'EVF50001', 'FIAT500EICON12345678', 259995.00, 16230.00, b'0', b'1', 5, 2),
    (105, 2022, 'Renault', 'Captur Techno 160 HK', 'Grå', 'Techno', 'Hybridvej 3, 2200 København N', 'EVR16001', 'RENAULTCAPTUR16012345', 274995.00, 17100.00, b'1', b'0', 6, 3),
    (120, 2023, 'Honda', 'Civic Sport 184 HK', 'Blå', 'Sport', 'Hybridvej 3, 2200 København N', 'EVH184S1', 'HONDACIVICSPORT1840001', 299995.00, 17750.00, b'1', b'1', 7, 2),
    (118, 2023, 'Honda', 'Civic Advance 184 HK', 'Rød', 'Advance', 'Hybridvej 3, 2200 København N', 'EVH184A1', 'HONDACIVICADV1840001', 319995.00, 18020.00, b'0', b'0', 7, 4);

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
                               FOREIGN KEY (car_id) REFERENCES car(car_id),
                               FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);
