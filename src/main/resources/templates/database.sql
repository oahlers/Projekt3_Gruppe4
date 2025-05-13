DROP DATABASE IF EXISTS bilabonnement;
CREATE DATABASE bilabonnement;
USE bilabonnement;

CREATE TABLE subscription_type (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   type_name VARCHAR(20) UNIQUE NOT NULL
);

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

CREATE TABLE availability_log (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  car_id INT NOT NULL,
                                  start_date DATE,
                                  end_date DATE,
                                  duration_days INT,
                                  FOREIGN KEY (car_id) REFERENCES car(car_id)
);

CREATE TABLE AvailabilityTracking (
                                      car_id INT PRIMARY KEY,
                                      start_date DATE NOT NULL,
                                      FOREIGN KEY (car_id) REFERENCES car(car_id)
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

INSERT INTO subscription_type (type_name)
VALUES ('unlimited'), ('limited');

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

INSERT INTO employees (fullname, username, password, role)
VALUES
    ('Demo Demosen', 'demo', 'demo', 'ADMIN'),
    ('Oliver Ahlers', 'oahlers', 'oahlers', 'EMPLOYEE'),
    ('Rasmus Guldborg', 'Rasmusg', 'rasmusg', 'EMPLOYEE'),
    ('Julius Gissel', 'juliusg', 'juliusg', 'EMPLOYEE'),
    ('Sandra Loveless', 'sandral', 'sandraloveless', 'EMPLOYEE'),
    ('Emilia Air', 'emiliaah', 'emiliah123', 'EMPLOYEE'),
    ('David Miller', 'david_miller', 'davidd123', 'EMPLOYEE'),
    ('Emma Jones', 'emma_jones', 'emmapassword', 'EMPLOYEE'),
    ('Frank White', 'frank_white', 'frankpass', 'EMPLOYEE'),
    ('Georgina Kaytranada', 'georginak', 'georgina123', 'EMPLOYEE');

INSERT INTO availability_log (car_id, start_date, end_date, duration_days)
VALUES
    (1, '2025-05-01', '2025-05-05', 4),
    (2, '2025-04-10', '2025-04-15', 5),
    (3, '2025-03-20', '2025-03-22', 2);

INSERT INTO rental (car_id, start_date, customer_name, customer_email, rental_months, ready_for_use_date, payment_time, transport_time, subscription_type_id)
VALUES
    (1, '2025-05-01', 'John Doe', 'john.doe@example.com', 6, '2025-11-01', 30, 5, 1),
    (2, '2025-04-15', 'Jane Smith', 'jane.smith@example.com', 3, '2025-07-15', 25, 4, 2),
    (3, '2025-03-01', 'Robert Evans', 'robert.evans@example.com', 12, '2025-03-01', 35, 7, 1),
    (4, '2025-04-01', 'Laura Black', 'laura.black@example.com', 24, '2025-04-01', 40, 10, 2),
    (5, '2025-05-01', 'Sara Green', 'sara.green@example.com', 6, '2025-05-01', 20, 6, 1);

INSERT INTO damage_report (car_id, price, employee_id, customer_email, report)
VALUES
    (1, 5000.00, 1, 'john.doe@example.com', 'Ridse på højre dør'),
    (2, 3000.00, 2, 'jane.smith@example.com', 'Skade på forrude');
