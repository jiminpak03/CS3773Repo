CREATE SCHEMA IF NOT EXISTS grocery_store;
USE grocery_store;

CREATE TABLE employee (
    employee_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(30) NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    email VARCHAR(45) NOT NULL UNIQUE,
    username VARCHAR(45) NOT NULL UNIQUE,
    user_password VARCHAR(45) NOT NULL,
    is_manager BOOLEAN NOT NULL
);

CREATE TABLE product (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(30) NOT NULL,
    price DOUBLE(7,2) NOT NULL,
    quantity BIGINT NOT NULL,
    sale BIGINT,
    product_description VARCHAR(300) NOT NULL,
    image VARCHAR(255) NULL
);

INSERT INTO employee (employee_id, last_name, first_name, email, username, user_password, is_manager)
VALUES
    (10673, 'Admin', 'Manager', 'admin.admin@gmail.com', 'admin123', 'admin', TRUE),
    (39256, 'Smith', 'John', 'john.smith@gmail.com', 'John123', 'password', FALSE);

INSERT INTO product (product_id, product_name, price, quantity, sale, product_description, image)
VALUES
    (4131, 'Apples', 1.99, 100, NULL, 'Fresh red apples from local farms.', 'images/apple.jpg'),
    (4011, 'Bananas', 0.79, 200, NULL, 'Organic bananas, sold by the bunch.', 'images/banana.jpg'),
    (4664, 'Tomato', 0.85, 78, NULL, 'Tomatoes, sold per item.', 'images/tomato.jpg'),
    (3082, 'Broccoli', 0.89, 150, NULL, 'Broccoli crowns, sold per pound.', 'images/broccoli.jpg'),
    (111234, 'Milk', 3.49, 167, 10, '2% Reduced Fat Milk, 1 gallon.', 'images/milk.jpg'),
    (111567, 'Bread', 3.79, 143, NULL, 'Whole wheat sandwich bread.', 'images/bread.jpg');