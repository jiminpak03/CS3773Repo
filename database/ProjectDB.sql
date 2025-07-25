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

CREATE TABLE orders (
	order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    order_price DOUBLE(7,2) NOT NULL,
	customer_lastname VARCHAR(30) NOT NULL,
    customer_firstname VARCHAR(30) NOT NULL,
    assigned_employee BIGINT,
	FOREIGN KEY (assigned_employee) REFERENCES employee(employee_id),
    execution_status ENUM('Pending', 'In Progress', 'Completed', 'Cancelled') NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    unit_price DOUBLE(7,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(253116, 4131, 5, 1.99), 
(253116, 111234, 2, 3.49),
(253116, 111567, 3, 3.79), 
(253116, 4664, 10, 0.85); 

INSERT INTO orders (order_id, order_time, order_price, customer_lastname, customer_firstname, assigned_employee, execution_status)
VALUES
	(253116, '2025-07-20 12:30:00', 145.63, 'Rose', 'Alexis', 39256, 'Pending');
    
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