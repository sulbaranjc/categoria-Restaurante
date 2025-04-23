-- BASE DE DATOS RESTAURANTE
DROP DATABASE IF EXISTS restaurante;
CREATE DATABASE restaurante
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE restaurante;

-- TABLA: Categorías
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    imagen VARCHAR(255),
    INDEX idx_nombre (nombre)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
