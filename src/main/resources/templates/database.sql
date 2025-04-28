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
                     is_car_available BIT NOT NULL
);

CREATE TABLE customer (
                          id VARCHAR(36) PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          cpr_nr VARCHAR(11) NOT NULL UNIQUE,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          phone VARCHAR(20) NOT NULL,
                          address VARCHAR(100) NOT NULL,
                          city VARCHAR(100) NOT NULL,
                          zip_code VARCHAR(10) NOT NULL,
                          return_address VARCHAR(100)
);

CREATE TABLE employee  (
                          employee_id INT AUTO_INCREMENT PRIMARY KEY,
                          fullname VARCHAR(100) NOT NULL,
                          username VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL
);


INSERT INTO employee (employee_id, username, password, fullname)
VALUES ('1', 'demo', 'demo', 'Demo demosen');

CREATE TABLE damage_report (
                               report_id INT AUTO_INCREMENT PRIMARY KEY,
                               car_id INT NOT NULL,
                               price DECIMAL(10,2) NOT NULL,
                               employee_id INT NOT NULL,
                               customer_id VARCHAR(36) NOT NULL,
                               FOREIGN KEY (car_id) REFERENCES car(car_id),
                               FOREIGN KEY (employee_id) REFERENCES employee(employee_id),
                               FOREIGN KEY (customer_id) REFERENCES customer(id)
);
