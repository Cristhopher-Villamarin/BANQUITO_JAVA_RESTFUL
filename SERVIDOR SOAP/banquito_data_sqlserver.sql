-- =============================================
-- BANCO BANQUITO - CARGA DE DATOS SISTEMA CORE
-- Datos ajustados a reglas de negocio del módulo de crédito
-- SQL SERVER 2022
-- =============================================

USE banquito_credito;
GO

-- =============================================
-- CLIENTES (5 registros ecuatorianos)
-- =============================================
INSERT INTO CLIENTE (cedula, nombre, fecha_nacimiento, estado_civil) VALUES
('1050423282', 'María José Andrade Calderón', '1990-03-15', 'C'),
('1752244770', 'Carlos Alberto Moncayo Rivadeneira', '1998-07-22', 'S'),
('1752244887', 'Ana Cristina Vallejo Torres', '1988-11-08', 'C'),
('1002410692', 'Jorge Luis Santillán Morales', '2001-05-30', 'S'),
('1000846822', 'Patricia Isabel Cevallos Benítez', '1987-09-18', 'D');
GO

-- =============================================
-- CUENTAS (5 cuentas)
-- =============================================
INSERT INTO CUENTA (numCuenta, cedula, saldo) VALUES
('20156789', '1050423282', 4500.00),
('20234567', '1752244770', 2800.00),
('20398765', '1752244887', 8500.00),
('20412345', '1002410692', 800.00),
('20587654', '1000846822', 3200.00);
GO

-- =============================================
-- MOVIMIENTOS (Últimos 3 meses: Agosto-Octubre 2025)
-- Total: 52 movimientos con DEP y RET
-- =============================================

-- MARÍA JOSÉ ANDRADE (20156789) - Agosto 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20156789', 'DEP', 2000.00, '2025-08-05'),
('20156789', 'DEP', 2500.00, '2025-08-12'),
('20156789', 'DEP', 1800.00, '2025-08-20'),
('20156789', 'RET', 300.00, '2025-08-15'),
('20156789', 'RET', 450.00, '2025-08-25');
GO

-- MARÍA JOSÉ ANDRADE - Septiembre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20156789', 'DEP', 2300.00, '2025-09-03'),
('20156789', 'DEP', 2100.00, '2025-09-15'),
('20156789', 'DEP', 1900.00, '2025-09-28'),
('20156789', 'RET', 280.00, '2025-09-10'),
('20156789', 'RET', 380.00, '2025-09-22');
GO

-- MARÍA JOSÉ ANDRADE - Octubre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20156789', 'DEP', 2200.00, '2025-10-05'),
('20156789', 'DEP', 2400.00, '2025-10-18'),
('20156789', 'DEP', 2000.00, '2025-10-28'),
('20156789', 'RET', 320.00, '2025-10-12'),
('20156789', 'RET', 400.00, '2025-10-25');
GO

-- CARLOS MONCAYO (20234567) - Agosto 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20234567', 'DEP', 1000.00, '2025-08-08'),
('20234567', 'DEP', 1200.00, '2025-08-22'),
('20234567', 'RET', 200.00, '2025-08-18'),
('20234567', 'RET', 280.00, '2025-08-28');
GO

-- CARLOS MONCAYO - Septiembre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20234567', 'DEP', 1100.00, '2025-09-05'),
('20234567', 'DEP', 1050.00, '2025-09-20'),
('20234567', 'RET', 250.00, '2025-09-12');
GO

-- CARLOS MONCAYO - Octubre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20234567', 'DEP', 1150.00, '2025-10-03'),
('20234567', 'DEP', 1080.00, '2025-10-17'),
('20234567', 'DEP', 1120.00, '2025-10-30'),
('20234567', 'RET', 260.00, '2025-10-10');
GO

-- ANA VALLEJO (20398765) - Agosto 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20398765', 'DEP', 2800.00, '2025-08-02'),
('20398765', 'DEP', 3200.00, '2025-08-16'),
('20398765', 'RET', 450.00, '2025-08-12'),
('20398765', 'RET', 550.00, '2025-08-25');
GO

-- ANA VALLEJO - Septiembre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20398765', 'DEP', 3100.00, '2025-09-04'),
('20398765', 'DEP', 2950.00, '2025-09-18'),
('20398765', 'RET', 480.00, '2025-09-10'),
('20398765', 'RET', 520.00, '2025-09-23');
GO

-- ANA VALLEJO - Octubre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20398765', 'DEP', 3050.00, '2025-10-06'),
('20398765', 'DEP', 2980.00, '2025-10-19'),
('20398765', 'RET', 500.00, '2025-10-14');
GO

-- JORGE SANTILLÁN (20412345) - Agosto 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20412345', 'DEP', 400.00, '2025-08-10'),
('20412345', 'DEP', 500.00, '2025-08-25'),
('20412345', 'RET', 150.00, '2025-08-20');
GO

-- JORGE SANTILLÁN - Septiembre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20412345', 'DEP', 450.00, '2025-09-08'),
('20412345', 'DEP', 420.00, '2025-09-22'),
('20412345', 'RET', 180.00, '2025-09-15');
GO

-- JORGE SANTILLÁN - Octubre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20412345', 'DEP', 480.00, '2025-10-07'),
('20412345', 'DEP', 460.00, '2025-10-21'),
('20412345', 'RET', 170.00, '2025-10-13');
GO

-- PATRICIA CEVALLOS (20587654) - Agosto 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20587654', 'DEP', 750.00, '2025-08-06'),
('20587654', 'DEP', 850.00, '2025-08-18'),
('20587654', 'RET', 180.00, '2025-08-14'),
('20587654', 'RET', 220.00, '2025-08-26');
GO

-- PATRICIA CEVALLOS - Septiembre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20587654', 'DEP', 800.00, '2025-09-07'),
('20587654', 'DEP', 780.00, '2025-09-21'),
('20587654', 'RET', 200.00, '2025-09-14');
GO

-- PATRICIA CEVALLOS - Octubre 2025
INSERT INTO MOVIMIENTO (numCuenta, tipo, valor, fecha) VALUES
('20587654', 'DEP', 820.00, '2025-10-04'),
('20587654', 'DEP', 790.00, '2025-10-16'),
('20587654', 'DEP', 810.00, '2025-10-28'),
('20587654', 'RET', 210.00, '2025-10-11');
GO

-- =============================================
-- CRÉDITOS
-- =============================================

INSERT INTO CREDITO (cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion)
VALUES ('1000846822', 1500.00, 1500.00, 6, 16.00, 262.29, 'CANCELADO', '2025-06-15', '2025-06-20');

INSERT INTO CREDITO (cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion)
VALUES ('1000846822', 2000.00, 2000.00, 12, 16.00, 180.52, 'ACTIVO', '2025-09-01', '2025-09-05');

INSERT INTO CREDITO (cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion)
VALUES ('1002410692', 5000.00, NULL, 12, 16.00, 0.00, 'CANCELADO', '2025-10-15', '2025-10-16');

INSERT INTO CREDITO (cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion)
VALUES ('1752244770', 3000.00, NULL, 12, 16.00, 0.00, 'CANCELADO', '2025-11-10', NULL);
GO

-- =============================================
-- AMORTIZACIÓN (Crédito ACTIVO de Patricia)
-- =============================================
-- Nota: En SQL Server, IDENTITY se auto-incrementa automáticamente
-- El idCredito = 2 corresponde al segundo crédito insertado

INSERT INTO AMORTIZACION (idCredito, numeroCuota, valorCuota, interesPagado, capitalPagado, saldo) VALUES
(2, 1, 180.52, 26.67, 153.85, 1846.15),
(2, 2, 180.52, 24.62, 155.90, 1690.25),
(2, 3, 180.52, 22.54, 157.98, 1532.27),
(2, 4, 180.52, 20.43, 160.09, 1372.18),
(2, 5, 180.52, 18.30, 162.22, 1209.96),
(2, 6, 180.52, 16.13, 164.39, 1045.57),
(2, 7, 180.52, 13.94, 166.58, 878.99),
(2, 8, 180.52, 11.72, 168.80, 710.19),
(2, 9, 180.52, 9.47, 171.05, 539.14),
(2, 10, 180.52, 7.19, 173.33, 365.81),
(2, 11, 180.52, 4.88, 175.64, 190.17),
(2, 12, 180.52, 2.54, 177.98, 12.19);
GO