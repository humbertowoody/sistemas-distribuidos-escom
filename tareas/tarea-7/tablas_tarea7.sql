/*
Este script define las tablas requeridas para ejecutar la tarea 7
Humberto Alejandro Ortega Alcocer
*/

-- Base de datos para la tarea 7 (la borra si ya existe)
DROP DATABASE IF EXISTS servicio_web;
CREATE DATABASE servicio_web;
USE servicio_web;

-- Tabla de articulos
CREATE TABLE articulos (
    id_articulo INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_articulo)
);

-- Tabla de fotos
CREATE TABLE fotos_articulos (
  foto LONGBLOB NOT NULL,
  id_articulo INT NOT NULL
);

-- Tabla de carrito
CREATE TABLE carrito (
  id_articulo INT NOT NULL,
  cantidad INT NOT NULL
);

-- Crear la llave foránea para las fotos.
ALTER TABLE fotos_articulos
ADD FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo);

-- Crear la llave foránea para el carrito.
ALTER TABLE carrito
ADD FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo);

-- Crear índice único para el nombre del artículo
CREATE UNIQUE INDEX nombre_articulo ON articulos(nombre);

-- Ejemplo de comando para importar este script en mysql 
-- (suponiendo que el archivo se llama tablas_tarea7.sql)
-- mysql -u root -p < tablas_tarea7.sql