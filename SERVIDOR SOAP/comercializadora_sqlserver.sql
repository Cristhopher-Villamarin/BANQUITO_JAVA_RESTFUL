-- =============================================
-- COMERCIALIZADORA DE ELECTRODOMÉSTICOS
-- SIN REGLAS DE NEGOCIO | SOLO ESTRUCTURA
-- SQL SERVER 2022
-- =============================================

-- Eliminar base de datos si existe
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'comercializadora')
BEGIN
    ALTER DATABASE comercializadora SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE comercializadora;
END
GO

-- Crear base de datos
CREATE DATABASE comercializadora
COLLATE Modern_Spanish_CI_AS;
GO

USE comercializadora;
GO

-- ELECTRODOMESTICO
CREATE TABLE ELECTRODOMESTICO (
    idElectrodomestico INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precioVenta DECIMAL(12,2) NOT NULL
);
GO

-- CLIENTE
CREATE TABLE CLIENTE (
    cedula VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(100)
);
GO

-- FACTURA
CREATE TABLE FACTURA (
    idFactura INT IDENTITY(1,1) PRIMARY KEY,
    cedula VARCHAR(10),
    fecha DATE NOT NULL,
    formaPago VARCHAR(20) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    descuento DECIMAL(12,2) NULL DEFAULT NULL,
    total DECIMAL(12,2) NOT NULL,
    estado VARCHAR(15) NOT NULL,
    CONSTRAINT FK_FACTURA_CLIENTE FOREIGN KEY (cedula) 
        REFERENCES CLIENTE(cedula)
        ON DELETE SET NULL 
        ON UPDATE CASCADE
);
GO

-- DETALLE_FACTURA
CREATE TABLE DETALLE_FACTURA (
    idDetalle INT IDENTITY(1,1) PRIMARY KEY,
    idFactura INT NOT NULL,
    idElectrodomestico INT NOT NULL,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(12,2) NOT NULL,
    subtotalLinea DECIMAL(12,2) NOT NULL,
    CONSTRAINT FK_DETALLE_FACTURA FOREIGN KEY (idFactura) 
        REFERENCES FACTURA(idFactura)
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT FK_DETALLE_ELECTRODOMESTICO FOREIGN KEY (idElectrodomestico) 
        REFERENCES ELECTRODOMESTICO(idElectrodomestico)
        ON DELETE NO ACTION 
        ON UPDATE CASCADE
);
GO