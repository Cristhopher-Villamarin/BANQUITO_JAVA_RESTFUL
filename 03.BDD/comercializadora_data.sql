-- =============================================
-- COMERCIALIZADORA DE ELECTRODOMÉSTICOS
-- CARGA DE DATOS COMPLETA
-- =============================================

USE comercializadora;

-- =============================================
-- ELECTRODOMÉSTICOS (Catálogo de productos)
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

-- =============================================
-- CLIENTES (Mismos del Banco Banquito + adicionales)
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

-- =============================================
-- FACTURAS (Ventas en Efectivo y Crédito Directo)
-- =============================================

-- Factura 1: Venta en EFECTIVO (33% descuento)
-- Cliente: Luis Fernando Méndez
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1803456789', '2025-10-15', 'EFECTIVO', 1299.99, 429.00, 870.99, 'PAGADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (1, 1, 1, 1299.99, 1299.99);

-- Factura 2: Venta en EFECTIVO (33% descuento)
-- Cliente: Carmen Vásquez - Compra múltiple
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('0987654321', '2025-10-18', 'EFECTIVO', 404.99, 133.65, 271.34, 'PAGADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(2, 4, 1, 249.99, 249.99),
(2, 6, 1, 89.99, 89.99),
(2, 13, 1, 65.00, 65.00);

-- Factura 3: Venta en CRÉDITO DIRECTO
-- Cliente: María José Andrade (cliente del banco)
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1050423282', '2025-10-20', 'CREDITO DIRECTO', 899.50, NULL, 899.50, 'APROBADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (3, 2, 1, 899.50, 899.50);

-- Factura 4: Venta en CRÉDITO DIRECTO
-- Cliente: Ana Cristina Vallejo (cliente del banco - perfil alto)
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1752244887', '2025-10-25', 'CREDITO DIRECTO', 2698.99, NULL, 2698.99, 'APROBADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(4, 5, 1, 1899.00, 1899.00),
(4, 10, 1, 799.99, 799.99);

-- Factura 5: Venta en EFECTIVO
-- Cliente: Roberto Zambrano
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1712345678', '2025-10-28', 'EFECTIVO', 599.00, 197.67, 401.33, 'PAGADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (5, 3, 1, 599.00, 599.00);

-- Factura 6: Venta en CRÉDITO DIRECTO
-- Cliente: Carlos Moncayo (cliente del banco)
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1752244770', '2025-11-02', 'CREDITO DIRECTO', 349.00, NULL, 349.00, 'APROBADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (6, 7, 1, 349.00, 349.00);

-- Factura 7: Venta en EFECTIVO - Compra múltiple
-- Cliente: Luis Fernando Méndez
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1803456789', '2025-11-05', 'EFECTIVO', 624.90, 206.22, 418.68, 'PAGADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(7, 8, 2, 45.50, 91.00),
(7, 9, 3, 125.00, 375.00),
(7, 11, 1, 179.90, 179.90),
(7, 12, 1, 299.00, 299.00);

-- Factura 8: Venta en CRÉDITO DIRECTO RECHAZADA
-- Cliente: Jorge Santillán (perfil bajo - no cumple requisitos)
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1002410692', '2025-11-08', 'CREDITO DIRECTO', 1299.99, NULL, 1299.99, 'RECHAZADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (8, 1, 1, 1299.99, 1299.99);

-- Factura 9: Venta en EFECTIVO
-- Cliente: Patricia Cevallos
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1000846822', '2025-11-10', 'EFECTIVO', 299.00, 98.67, 200.33, 'PAGADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES (9, 12, 1, 299.00, 299.00);

-- Factura 10: Venta en CRÉDITO DIRECTO
-- Cliente: Ana Vallejo (segunda compra)
INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) 
VALUES ('1752244887', '2025-11-12', 'CREDITO DIRECTO', 1348.99, NULL, 1348.99, 'APROBADA');

INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea)
VALUES 
(10, 14, 1, 749.00, 749.00),
(10, 15, 1, 599.99, 599.99);