-- =============================================
-- BANCO BANQUITO - MÓDULO DE CRÉDITO
-- MySQL 8.0 | 100% EJECUTABLE
-- =============================================

DROP DATABASE IF EXISTS banquito_credito;
CREATE DATABASE banquito_credito CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE banquito_credito;

-- CLIENTE
CREATE TABLE CLIENTE (
    cedula VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado_civil CHAR(1) NOT NULL CHECK (estado_civil IN ('S','C','D','V'))
    -- CHECK de edad eliminado → se valida en la aplicación
) ENGINE=InnoDB;

-- CUENTA
CREATE TABLE CUENTA (
    numCuenta VARCHAR(8) PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL,
    saldo DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (saldo >= 0),
    FOREIGN KEY (cedula) REFERENCES CLIENTE(cedula)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- MOVIMIENTO
CREATE TABLE MOVIMIENTO (
    codigoMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    numCuenta VARCHAR(8) NOT NULL,
    tipo CHAR(3) NOT NULL CHECK (tipo IN ('DEP','RET')),
    valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
    fecha DATE NOT NULL DEFAULT (CURDATE()),
    FOREIGN KEY (numCuenta) REFERENCES CUENTA(numCuenta)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_mov_ultimos3 (numCuenta, fecha DESC)
) ENGINE=InnoDB;

-- CRÉDITO
CREATE TABLE CREDITO (
    idCredito INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL,
    montoSolicitado DECIMAL(12,2) NOT NULL CHECK (montoSolicitado > 0),
    montoAprobado DECIMAL(12,2),
    plazoMeses INT NOT NULL CHECK (plazoMeses BETWEEN 3 AND 24),
    tasaAnual DECIMAL(5,2) NOT NULL DEFAULT 16.00,
    cuotaFija DECIMAL(12,2) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
        CHECK (estado IN ('ACTIVO','CANCELADO')),
    fechaSolicitud DATE NOT NULL DEFAULT (CURDATE()),
    fechaAprobacion DATE,
    FOREIGN KEY (cedula) REFERENCES CLIENTE(cedula)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_monto_aprobado 
        CHECK (montoAprobado IS NULL OR montoAprobado <= montoSolicitado)
    -- CHECK de crédito activo eliminado → se valida en aplicación
) ENGINE=InnoDB;

-- AMORTIZACIÓN
CREATE TABLE AMORTIZACION (
    idAmortizacion INT AUTO_INCREMENT PRIMARY KEY,
    idCredito INT NOT NULL,
    numeroCuota INT NOT NULL CHECK (numeroCuota >= 1),
    valorCuota DECIMAL(12,2) NOT NULL,
    interesPagado DECIMAL(12,2) NOT NULL,
    capitalPagado DECIMAL(12,2) NOT NULL,
    saldo DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (idCredito) REFERENCES CREDITO(idCredito)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uq_credito_cuota (idCredito, numeroCuota)
) ENGINE=InnoDB;