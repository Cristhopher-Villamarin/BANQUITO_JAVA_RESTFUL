-- =============================================
-- BANCO BANQUITO - MÓDULO DE CRÉDITO
-- SIN REGLAS DE NEGOCIO | SOLO ESTRUCTURA
-- =============================================

DROP DATABASE IF EXISTS banquito_credito;
CREATE DATABASE banquito_credito CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE banquito_credito;

-- CLIENTE
CREATE TABLE CLIENTE (
    cedula VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado_civil CHAR(1) NOT NULL
) ENGINE=InnoDB;

-- CUENTA
CREATE TABLE CUENTA (
    numCuenta VARCHAR(8) PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL,
    saldo DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (cedula) REFERENCES CLIENTE(cedula)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- MOVIMIENTO
CREATE TABLE MOVIMIENTO (
    codigoMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    numCuenta VARCHAR(8) NOT NULL,
    tipo CHAR(3) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (numCuenta) REFERENCES CUENTA(numCuenta)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- CRÉDITO
CREATE TABLE CREDITO (
    idCredito INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL,
    montoSolicitado DECIMAL(12,2) NOT NULL,
    montoAprobado DECIMAL(12,2),
    plazoMeses INT NOT NULL,
    tasaAnual DECIMAL(5,2) NOT NULL,
    cuotaFija DECIMAL(12,2) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
        CHECK (estado IN ('ACTIVO','CANCELADO')),
    fechaSolicitud DATE NOT NULL DEFAULT (CURDATE()),
    fechaAprobacion DATE,
    FOREIGN KEY (cedula) REFERENCES CLIENTE(cedula)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- AMORTIZACIÓN
CREATE TABLE AMORTIZACION (
    idAmortizacion INT AUTO_INCREMENT PRIMARY KEY,
    idCredito INT NOT NULL,
    numeroCuota INT NOT NULL,
    valorCuota DECIMAL(12,2) NOT NULL,
    interesPagado DECIMAL(12,2) NOT NULL,
    capitalPagado DECIMAL(12,2) NOT NULL,
    saldo DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (idCredito) REFERENCES CREDITO(idCredito)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;