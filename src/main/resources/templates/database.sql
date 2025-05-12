DROP DATABASE IF EXISTS bilabonnement;
CREATE DATABASE bilabonnement;
USE bilabonnement;

CREATE TABLE subscription_type (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   type_name VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO subscription_type (type_name)
VALUES ('unlimited'),  ('limited');

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
                     license_plate VARCHAR(100) UNIQUE NOT NULL,
                     price DECIMAL(10,2) NOT NULL,
                     registration_fee DECIMAL(10,2) NOT NULL,
                     image VARCHAR(255),
                     isAvailableForLoan BIT NOT NULL,
                     isReadyForUse BIT NOT NULL
);

CREATE TABLE employees (
                           employee_id INT AUTO_INCREMENT PRIMARY KEY,
                           fullname VARCHAR(100) NOT NULL,
                           username VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE'
);

CREATE TABLE rental (
                        rental_id INT AUTO_INCREMENT PRIMARY KEY,
                        car_id INT NOT NULL,
                        start_date DATE NOT NULL,
                        end_date DATE,
                        customer_name VARCHAR(100),
                        customer_email VARCHAR(100),
                        rental_months INT NOT NULL,
                        ready_for_use_date DATE,
                        payment_time INT,
                        transport_time INT,
                        subscription_type_id INT NOT NULL,
                        FOREIGN KEY (car_id) REFERENCES car(car_id),
                        FOREIGN KEY (subscription_type_id) REFERENCES subscription_type(id)
);

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

INSERT INTO car (car_emission, year, brand, model, color, equipment_level, vehicle_number, chassis_number, license_plate, price, registration_fee, image, isAvailableForLoan, isReadyForUse)
VALUES
    (0, 2024, 'NAVOR', 'E5 ROCK 218 HK', 'Sort', 'Premium', 'EVN12345', 'NAVORE5ROCK12345678', 'AB12345', 329995.00, 18420.00, '/img/rock.jpg', b'1', b'0'),
    (0, 2023, 'Fiat', '500e Icon Pack 118 HK', 'Hvid', 'Icon Pack', 'EVF50001', 'FIAT500EICON12345678', 'XY98765', 259995.00, 16230.00, '/img/captur.jpg', b'1', b'0'),
    (100, 2023, 'Tesla', 'Model Y Long Range', 'Blå', 'Standard', 'EVT10001', 'TESLAMODELYLONG12345678', 'CD54321', 689995.00, 29550.00, '/img/civicAdvance.jpg', b'1', b'0'),
    (50, 2023, 'BMW', 'iX3', 'Grå', 'Xdrive', 'EVB50001', 'BMWIX3123456789', 'EF12345', 799995.00, 41250.00, '/img/civicTechno.jpg', b'0', b'1'),
    (0, 2022, 'Nissan', 'Leaf 40 kWh', 'Rød', 'N-Connecta', 'EVN20001', 'NISSANLEAF12345678', 'GH67890', 219995.00, 11850.00, '/img/civicSport.jpg', b'1', b'0'),
    (0, 2021, 'Renault', 'Zoe 52 kWh', 'Grøn', 'Intens', 'EVR50001', 'RENAULTZOE12345678', 'IJ12345', 189995.00, 8200.00, '/img/civicAdvance.jpg', b'0', b'0'),
    (0, 2023, 'Audi', 'Q4 e-tron', 'Blå', 'S Line', 'EVA40001', 'AUDIQ4ETRON12345678', 'KL98765', 619995.00, 23400.00, '/img/civicTechno.jpg', b'0', b'0'),
    (0, 2024, 'Volkswagen', 'ID.4', 'Sort', 'Pro Performance', 'EVW20001', 'VOLKSWAGENID4123456789', 'MN54321', 479995.00, 20750.00, '/img/civicSport.jpg', b'0', b'0');

INSERT INTO employees (employee_id, username, password, fullname, role)
VALUES
    (1, 'demo', 'demo', 'Demo Demosen', 'ADMIN'),
    (2, 'oahlers', 'oahlers', 'Oliver Ahlers', 'EMPLOYEE'),
    (3, 'Rasmusg', 'rasmusg', 'Rasmus Guldborg', 'EMPLOYEE'),
    (4, 'juliusg', 'juliusg', 'Julius Gissel', 'EMPLOYEE'),
    (5, 'sandral', 'sandraloveless', 'Sandra Loveless', 'EMPLOYEE'),
    (6, 'emiliaah', 'emiliah123', 'Emilia Air', 'EMPLOYEE'),
    (7, 'david_miller', 'davidd123', 'David Miller', 'EMPLOYEE'),
    (8, 'emma_jones', 'emmapassword', 'Emma Jones', 'EMPLOYEE'),
    (9, 'frank_white', 'frankpass', 'Frank White', 'EMPLOYEE'),
    (10, 'georginak', 'georgina123', 'Georgina Kaytranada', 'EMPLOYEE');
INSERT INTO rental (car_id, start_date, customer_name, customer_email, rental_months, ready_for_use_date, payment_time, transport_time, subscription_type_id)
VALUES
    (1, '2025-05-01', 'John Doe', 'john.doe@example.com', 6, '2025-11-01', 30, 5, 1),  -- subscription_type_id = 1 for 'unlimited'
    (2, '2025-04-15', 'Jane Smith', 'jane.smith@example.com', 3, '2025-07-15', 25, 4, 2); -- subscription_type_id = 2 for 'limited'

INSERT INTO damage_report (car_id, price, employee_id, customer_email, report)
VALUES
    (1, 5000.00, 1, 'john.doe@example.com', 'Ridse på højre dør'),
    (2, 3000.00, 2, 'jane.smith@example.com', 'Skade på forrude');
