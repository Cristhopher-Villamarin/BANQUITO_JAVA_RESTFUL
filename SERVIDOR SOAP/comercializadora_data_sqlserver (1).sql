-- =============================================
-- COMERCIALIZADORA DE ELECTRODOMÉSTICOS
-- CARGA DE DATOS COMPLETA
-- SQL SERVER 2022
-- =============================================
-- NOTA: Ejecutar primero el script de creación de base de datos

USE comercializadora;
GO

-- =============================================
-- LIMPIAR DATOS EXISTENTES (en orden por dependencias)
-- =============================================
IF EXISTS (SELECT 1 FROM DETALLE_FACTURA)
BEGIN
    DELETE FROM DETALLE_FACTURA;
    DBCC CHECKIDENT ('DETALLE_FACTURA', RESEED, 0);
END

IF EXISTS (SELECT 1 FROM FACTURA)
BEGIN
    DELETE FROM FACTURA;
    DBCC CHECKIDENT ('FACTURA', RESEED, 0);
END

IF EXISTS (SELECT 1 FROM ELECTRODOMESTICO)
BEGIN
    DELETE FROM ELECTRODOMESTICO;
    DBCC CHECKIDENT ('ELECTRODOMESTICO', RESEED, 0);
END

IF EXISTS (SELECT 1 FROM CLIENTE)
BEGIN
    DELETE FROM CLIENTE;
END
GO

-- =============================================
-- ELECTRODOMÉSTICOS (Catálogo de productos)
-- 15 productos - IDENTITY automático (1-15)
-- =============================================
INSERT INTO ELECTRODOMESTICO (nombre, precioVenta) VALUES
('Refrigeradora LG 18 pies', 1299.99),
('Lavadora Samsung 20 libras', 899.50),
('Cocina Indurama 6 quemadores', 599.00),
('Microondas Panasonic 1.2 pies', 249.99),
('Televisor Sony 55 pulgadas 4K', 1899.00),
('Licuadora Oster 10 velocidades', 89.99),
('Aspiradora Electrolux 2000W', 349.00),
('Plancha Philips a vapor', 45.50),
('Ventilador Samurai 20 pulgadas', 125.00),
('Aire Acondicionado LG 12000 BTU', 799.99),
('Horno Eléctrico Black+Decker', 179.90),
('Batidora KitchenAid profesional', 299.00),
('Cafetera Oster 12 tazas', 65.00),
('Secadora de ropa Mabe 18 libras', 749.00),
('Freezer Indurama horizontal', 599.99);
GO

-- =============================================
-- CLIENTES (8 clientes)
-- =============================================
INSERT INTO CLIENTE (cedula, nombre) VALUES
('1050423282', 'María José Andrade Calderón'),
('1752244770', 'Carlos Alberto Moncayo Rivadeneira'),
('1752244887', 'Ana Cristina Vallejo Torres'),
('1002410692', 'Jorge Luis Santillán Morales'),
('1000846822', 'Patricia Isabel Cevallos Benítez'),
('1803456789', 'Luis Fernando Méndez Ortiz'),
('0987654321', 'Carmen Lucía Vásquez Mora'),
('1712345678', 'Roberto Carlos Zambrano Cruz');
GO

-- =============================================
-- FACTURAS Y DETALLES (10 facturas con 17 líneas de detalle)
-- =============================================

-- Factura 1: Luis Fernando Méndez - EFECTIVO (33% descuento)
DECLARE @idFact1 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1803456789', '2025-10-15', 'EFECTIVO', 1299.99, 429.00, 870.99, 'PAGADA');
SET @idFact1 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (@idFact1, 1, 1, 1299.99, 1299.99);
GO

-- Factura 2: Carmen Vásquez - EFECTIVO (33% descuento) - Compra múltiple
DECLARE @idFact2 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('0987654321', '2025-10-18', 'EFECTIVO', 404.99, 133.65, 271.34, 'PAGADA');
SET @idFact2 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(@idFact2, 4, 1, 249.99, 249.99),
(@idFact2, 6, 1, 89.99, 89.99),
(@idFact2, 13, 1, 65.00, 65.00);
GO

-- Factura 3: María José Andrade - CRÉDITO DIRECTO
DECLARE @idFact3 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1050423282', '2025-10-20', 'CREDITO DIRECTO', 899.50, NULL, 899.50, 'APROBADA');
SET @idFact3 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (@idFact3, 2, 1, 899.50, 899.50);
GO

-- Factura 4: Ana Cristina Vallejo - CRÉDITO DIRECTO (perfil alto)
DECLARE @idFact4 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1752244887', '2025-10-25', 'CREDITO DIRECTO', 2698.99, NULL, 2698.99, 'APROBADA');
SET @idFact4 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(@idFact4, 5, 1, 1899.00, 1899.00),
(@idFact4, 10, 1, 799.99, 799.99);
GO

-- Factura 5: Roberto Zambrano - EFECTIVO
DECLARE @idFact5 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1712345678', '2025-10-28', 'EFECTIVO', 599.00, 197.67, 401.33, 'PAGADA');
SET @idFact5 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (@idFact5, 3, 1, 599.00, 599.00);
GO

-- Factura 6: Carlos Moncayo - CRÉDITO DIRECTO
DECLARE @idFact6 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1752244770', '2025-11-02', 'CREDITO DIRECTO', 349.00, NULL, 349.00, 'APROBADA');
SET @idFact6 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (@idFact6, 7, 1, 349.00, 349.00);
GO

-- Factura 7: Luis Fernando Méndez - EFECTIVO - Compra múltiple
DECLARE @idFact7 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1803456789', '2025-11-05', 'EFECTIVO', 624.90, 206.22, 418.68, 'PAGADA');
SET @idFact7 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(@idFact7, 8, 2, 45.50, 91.00),
(@idFact7, 9, 3, 125.00, 375.00),
(@idFact7, 11, 1, 179.90, 179.90),
(@idFact7, 12, 1, 299.00, 299.00);
GO

-- Factura 8: Jorge Santillán - CRÉDITO DIRECTO RECHAZADA
DECLARE @idFact8 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1002410692', '2025-11-08', 'CREDITO DIRECTO', 1299.99, NULL, 1299.99, 'RECHAZADA');
SET @idFact8 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (@idFact8, 1, 1, 1299.99, 1299.99);
GO

-- Factura 9: Patricia Cevallos - EFECTIVO
DECLARE @idFact9 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1000846822', '2025-11-10', 'EFECTIVO', 299.00, 98.67, 200.33, 'PAGADA');
SET @idFact9 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (@idFact9, 12, 1, 299.00, 299.00);
GO

-- Factura 10: Ana Vallejo - CRÉDITO DIRECTO (segunda compra)
DECLARE @idFact10 INT;
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1752244887', '2025-11-12', 'CREDITO DIRECTO', 1348.99, NULL, 1348.99, 'APROBADA');
SET @idFact10 = SCOPE_IDENTITY();

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(@idFact10, 14, 1, 749.00, 749.00),
(@idFact10, 15, 1, 599.99, 599.99);
GO