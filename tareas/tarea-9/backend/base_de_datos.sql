-- Sistemas Distribuidos - Tarea 9 
-- Script para generar la base de datos requerida por la tarea.
-- Humberto Alejandro Ortega Alcocer
-- 2016630495

-- Eliminamos la base de datos si existe.
DROP DATABASE IF EXISTS tarea9;

-- Creamos la base de datos y la seleccionamos.
CREATE DATABASE tarea9;
USE tarea9;

-- Creamos la tabla de Artículos.
CREATE TABLE articulos(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    descripcion VARCHAR(512) NOT NULL,
    fotografia LONGBLOB,
    PRIMARY KEY (id)
);

-- Creamos la tabla de Carrito.
CREATE TABLE carrito (
    articulo_id INT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (articulo_id),
    FOREIGN KEY (articulo_id) REFERENCES articulos(id)
);

