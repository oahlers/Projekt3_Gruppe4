DROP DATABASE IF EXISTS DataBase;
CREATE DATABASE DataBase;
USE DataBase;

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
                     price DOUBLE NOT NULL,
                     registration_fee DOUBLE NOT NULL,
                     is_car_available BIT NOT NULL
);


INSERT INTO customer (name, description, price) VALUES
                                                         ('Øl', 'Den kan drikkes', 18);

CREATE TABLE employee (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, password) VALUES
                                           ('user1', 'password1');

CREATE TABLE damage_report (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT NOT NULL,
                           name VARCHAR(100) NOT NULL,
                           description VARCHAR(255),
                           pincode VARCHAR(255),
                           share_token VARCHAR(100) UNIQUE,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE wishlist_products (
                                   wishlist_id INT NOT NULL,
                                   product_id INT NOT NULL,
                                   PRIMARY KEY (wishlist_id, product_id),
                                   FOREIGN KEY (wishlist_id) REFERENCES wishlists(id) ON DELETE CASCADE,
                                   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

