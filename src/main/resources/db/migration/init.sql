CREATE DATABASE eshop;
USE eshop;

CREATE TABLE products
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NULL,
    price    INT          NOT NULL,
    order_id INT          NULL
);

CREATE TABLE orders
(
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    creation_date         date         NOT NULL,
    total_price           INT          NOT NULL,
    customer_name         VARCHAR(255) NULL,
    customer_address      VARCHAR(255) NULL,
    customer_email        VARCHAR(255) NULL,
    customer_phone_number VARCHAR(255) NULL
);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);