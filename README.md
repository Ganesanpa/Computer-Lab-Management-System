# Computer-Lab-Management-System


SQL QUARIES FOR DATABASE

CREATE DATABASE lab_management;
USE lab_management;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20)
);

INSERT INTO users (name, email, username, password, role)
VALUES ('Admin User', 'admin@example.com', 'admin', 'admin123', 'Admin')
ON DUPLICATE KEY UPDATE username=username;


select *from users;


CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lab_name VARCHAR(100),
    reserved_date DATE,
    reserved_time VARCHAR(20),
    reserved_by VARCHAR(50)
);
select*from reservations;

CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    student_id VARCHAR(50) DEFAULT '', -- optional
    lab_name VARCHAR(100) DEFAULT '',
    date DATE NOT NULL,
    status VARCHAR(10) NOT NULL
);
ALTER TABLE attendance ADD COLUMN date DATE;
DESCRIBE attendance;


CREATE TABLE IF NOT EXISTS equipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS software (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    version VARCHAR(50),
    license VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS maintenance_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50),     -- e.g., Lab, Equipment, Software
    name VARCHAR(100),
    maintenance_date DATE,
    notes TEXT
);




select * from  users;
select *from reservation;
select *from attendance;
select *from equipment;
select *from software;
select *from maintenance_logs;


