DROP DATABASE IF EXISTS database;
CREATE DATABASE database;
USE database;

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

CREATE TABLE employee (
                          employee_id INT AUTO_INCREMENT PRIMARY KEY,
                          firstname VARCHAR(100) NOT NULL,
                          lastname VARCHAR(100) NOT NULL,
                          user_type VARCHAR(50) NOT NULL,
                          username VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL
);


INSERT INTO employee (username, password, firstname, lastname, user_type)
VALUES ('user1', 'password1', 'First', 'Last', 'admin');

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
