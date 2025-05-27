-- Database med oprettelse af tabeller og tilhørende eksempler
    -- Julis Gissel, Rasmus Guldborg Pedersen og Oliver Ahlers


-- Dropper databasen, hvis den eksisterer, og opretter en ny.
DROP DATABASE IF EXISTS bilabonnement;

CREATE DATABASE bilabonnement;
USE bilabonnement;

-- Opretter tabellen for abonnementstyper.
CREATE TABLE subscription_type (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   type_name VARCHAR(20) UNIQUE NOT NULL
);

-- Opretter tabellen for biler med opdaterede kolonnenavne isRented og needsDamageReport.
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
                     isRented BIT NOT NULL,
                     needsDamageReport BIT NOT NULL
);

-- Opretter tabellen for medarbejdere.
CREATE TABLE employees (
                           employee_id INT AUTO_INCREMENT PRIMARY KEY,
                           fullname VARCHAR(100) NOT NULL,
                           username VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE'
);

-- Opretter tabellen for tilgængelighedslog.
CREATE TABLE availability_log (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  car_id INT NOT NULL,
                                  start_date DATE,
                                  end_date DATE,
                                  duration_days INT,
                                  FOREIGN KEY (car_id) REFERENCES car(car_id)
);

-- Opretter tabellen for tilgængelighedssporing.
CREATE TABLE AvailabilityTracking (
                                      car_id INT PRIMARY KEY,
                                      start_date DATE NOT NULL,
                                      FOREIGN KEY (car_id) REFERENCES car(car_id)
);

-- Opretter tabellen for lejeaftaler med ny kolonne isPurchased.
CREATE TABLE rental (
                        rental_id INT AUTO_INCREMENT PRIMARY KEY,
                        car_id INT NOT NULL,
                        start_date DATE NOT NULL,
                        end_date DATE,
                        customer_name VARCHAR(100),
                        customer_email VARCHAR(100),
                        delivery_address VARCHAR(255),
                        rental_months INT NOT NULL,
                        ready_for_use_date DATE,
                        payment_time INT,
                        transport_time INT,
                        subscription_type_id INT NOT NULL,
                        mileage INT NOT NULL,
                        isPurchased BIT NOT NULL DEFAULT 0,
                        FOREIGN KEY (car_id) REFERENCES car(car_id),
                        FOREIGN KEY (subscription_type_id) REFERENCES subscription_type(id)
);

-- Opretter tabellen for skadesrapporter.
CREATE TABLE damage_report (
                               report_id INT AUTO_INCREMENT PRIMARY KEY,
                               car_id INT NOT NULL,
                               employee_id INT NOT NULL,
                               customer_email VARCHAR(100),
                               mileage INT NOT NULL,
                               FOREIGN KEY (car_id) REFERENCES car(car_id),
                               FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Opretter tabellen for skader tilknyttet skadesrapporter.
CREATE TABLE damage (
                        damage_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        report_id INT NOT NULL,
                        description TEXT NOT NULL,
                        price DECIMAL(10,2) NOT NULL,
                        FOREIGN KEY (report_id) REFERENCES damage_report(report_id)
);

-- Indsætter testdata for abonnementstyper.
INSERT INTO subscription_type (type_name)
VALUES ('UNLIMITED'), ('LIMITED');

-- Indsætter testdata for biler med opdaterede feltnavne.
INSERT INTO car (car_emission, year, brand, model, color, equipment_level, vehicle_number, chassis_number, license_plate, price, registration_fee, image, isRented, needsDamageReport)
VALUES
    (0, 2024, 'NAVOR', 'E5 ROCK 218 HK', 'Sort', 'Premium', 'EVN12345', 'NAVORE5ROCK12345678', 'AB12345', 329995.00, 18420.00, '/img/rock.jpg', b'1', b'1'),
    (0, 2023, 'Fiat', '500e Icon Pack 118 HK', 'Hvid', 'Icon Pack', 'EVF50001', 'FIAT500EICON12345678', 'XY98765', 259995.00, 16230.00, '/img/captur.jpg', b'1', b'1'),
    (100, 2023, 'Tesla', 'Model Y Long Range', 'Blå', 'Standard', 'EVT10001', 'TESLAMODELYLONG12345678', 'CD54321', 689995.00, 29550.00, '/img/civicAdvance.jpg', b'1', b'0'),
    (50, 2023, 'BMW', 'iX3', 'Grå', 'Xdrive', 'EVB50001', 'BMWIX3123456789', 'EF12345', 799995.00, 41250.00, '/img/civicTechno.jpg', b'1', b'1'),
    (0, 2022, 'Nissan', 'Leaf 40 kWh', 'Rød', 'N-Connecta', 'EVN20001', 'NISSANLEAF12345678', 'GH67890', 219995.00, 11850.00, '/img/sivicSport.jpg', b'1', b'0'),
    (0, 2021, 'Renault', 'Zoe 52 kWh', 'Grøn', 'Intens', 'EVR50001', 'RENAULTZOE12345678', 'IJ12345', 189995.00, 8200.00, '/img/civicAdvance.jpg', b'0', b'0'),
    (0, 2023, 'Audi', 'Q4 e-tron', 'Blå', 'S Line', 'EVA40001', 'AUDIQ4ETRON12345678', 'KL98765', 619995.00, 23400.00, '/img/civicTechno.jpg', b'0', b'0'),
    (0, 2024, 'Volkswagen', 'ID.4', 'Sort', 'Pro Performance', 'EVW20001', 'VOLKSWAGENID4123456789', 'MN54321', 479995.00, 20750.00, '/img/storVarebil.jpg', b'0', b'0'),
    (0, 2023, 'Peugeot', 'e-208', 'Hvid', 'GT', 'EVP90001', 'PEUGEOTE20812345678', 'OP12345', 299995.00, 17500.00, '/img/e208.jpg', b'1', b'1'),
    (0, 2022, 'Hyundai', 'Kona Electric', 'Grå', 'Trend', 'EVH90002', 'HYUNDAIKONA12345678', 'QR67890', 279995.00, 16500.00, '/img/kona.jpg', b'1', b'1');

-- Indsætter testdata for medarbejdere.
INSERT INTO employees (fullname, username, password, role)
VALUES
    ('Demo Demosen', 'demo', 'demo', 'ADMIN'),
    ('Oliver Ahlers', 'oahlers', 'oliver456', 'EMPLOYEE'),
    ('Rasmus Guldborg', 'Rasmusg', 'rasmus789', 'EMPLOYEE'),
    ('Julius Gissel', 'juliusg', 'julius101', 'EMPLOYEE'),
    ('Sandra Loveless', 'sandral', 'sandra112', 'EMPLOYEE'),
    ('Emilia Air', 'emiliaah', 'emilia123', 'EMPLOYEE'),
    ('David Miller', 'david_miller', 'david456', 'EMPLOYEE'),
    ('Emma Jones', 'emma_jones', 'emma789', 'EMPLOYEE'),
    ('Frank White', 'frank_white', 'frank101', 'EMPLOYEE'),
    ('Georgina Kaytranada', 'georginak', 'georgina112', 'EMPLOYEE');

-- Indsætter testdata for tilgængelighedslog.
INSERT INTO availability_log (car_id, start_date, end_date, duration_days)
VALUES
    (1, '2025-05-01', '2025-05-05', 4),
    (2, '2025-04-10', '2025-04-15', 5),
    (3, '2025-03-20', '2025-03-22', 2);

-- Indsætter testdata for lejeaftaler med isPurchased default til 0.
INSERT INTO rental (car_id, start_date, customer_name, customer_email, delivery_address, rental_months, ready_for_use_date, payment_time, transport_time, subscription_type_id, mileage, isPurchased)
VALUES
    (1, '2025-06-01', 'John Doe', 'john.doe@example.com', 'Roskildevej 12, 2000 Frederiksberg', 6, '2025-12-01', 30, 5, 1, 2000, 0),
    (2, '2025-06-15', 'Jane Smith', 'jane.smith@example.com', 'Nørrebrogade 84, 2200 København N', 5, '2025-11-15', 25, 4, 2, 1500, 0),
    (3, '2025-07-01', 'Robert Evans', 'robert.evans@example.com', 'Hovedgaden 45, 4000 Roskilde', 12, '2026-07-01', 35, 7, 1, 2500, 0),
    (4, '2025-08-01', 'Laura Black', 'laura.black@example.com', 'Østerbrogade 100, 2100 København Ø', 24, '2027-08-01', 40, 10, 1, 3000, 0),
    (5, '2025-06-01', 'Sara Green', 'sara.green@example.com', 'Amagerbrogade 200, 2300 København S', 6, '2025-12-01', 20, 6, 1, 1750, 0),
    (9, '2024-12-15', 'Michael Brown', 'michael.brown@example.com', 'Vesterbrogade 50, 1620 København V', 5, '2025-05-15', 28, 3, 2, 1800, 0),
    (10, '2024-12-15', 'Emily Davis', 'emily.davis@example.com', 'Frederiksberg Allé 25, 1820 Frederiksberg', 5, '2025-05-15', 30, 4, 2, 1600, 0);

-- Indsætter testdata for skadesrapporter.
INSERT INTO damage_report (car_id, employee_id, customer_email, mileage)
VALUES
    (1, 1, 'john.doe@example.com', 2100),
    (2, 2, 'jane.smith@example.com', 1600);

-- Indsætter testdata for skader.
INSERT INTO damage (report_id, description, price)
VALUES
    (1, 'Ridse på højre dør', 1500.00),
    (1, 'Sprække i forrude', 3000.00),
    (2, 'Skade på bagkofanger', 2000.00);