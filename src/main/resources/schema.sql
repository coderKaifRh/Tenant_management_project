-- Database Schema for TAK Limited (Tenant Management System)

CREATE DATABASE IF NOT EXISTS tak_limited;
USE tak_limited;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- 2. Owner Details
CREATE TABLE IF NOT EXISTS owner_info (
    info_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Tenant Details
CREATE TABLE IF NOT EXISTS tenant_info (
    info_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. Flats Table
CREATE TABLE IF NOT EXISTS flats (
    flat_id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL,
    location VARCHAR(100) NOT NULL,
    rent DOUBLE NOT NULL,
    size INT NOT NULL,
    bedroom INT NOT NULL,
    washroom INT NOT NULL,
    status VARCHAR(20) DEFAULT 'Available',
    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. Flat Images Table
CREATE TABLE IF NOT EXISTS flat_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    flat_id INT NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    FOREIGN KEY (flat_id) REFERENCES flats(flat_id) ON DELETE CASCADE
);

-- 6. Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    flat_id INT NOT NULL,
    tenant_id INT NOT NULL,
    FOREIGN KEY (flat_id) REFERENCES flats(flat_id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 7. Transport Service Table
CREATE TABLE IF NOT EXISTS transport (
    transport_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    pickup_address VARCHAR(255) NOT NULL,
    moving_date VARCHAR(50) NOT NULL,
    time VARCHAR(50) NOT NULL,
    truck_size VARCHAR(50) NOT NULL,
    manpower INT NOT NULL,
    cost DOUBLE NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);
