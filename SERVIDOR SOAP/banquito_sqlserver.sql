-- =============================================
-- BANCO BANQUITO - MÓDULO DE CRÉDITO
-- SIN REGLAS DE NEGOCIO | SOLO ESTRUCTURA
-- SQL SERVER 2022
-- =============================================

-- Eliminar base de datos si existe
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'banquito_credito')
BEGIN
    ALTER DATABASE banquito_credito SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE banquito_credito;
END
GO

-- Crear base de datos
CREATE DATABASE banquito_credito
COLLATE Modern_Spanish_CI_AS;
GO

USE banquito_credito;
GO

-- CLIENTE
CREATE TABLE CLIENTE (
    cedula VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado_civil CHAR(1) NOT NULL
);
GO

-- CUENTA
CREATE TABLE CUENTA (
    numCuenta VARCHAR(8) PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL,
    saldo DECIMAL(12,2) NOT NULL,
    CONSTRAINT FK_CUENTA_CLIENTE FOREIGN KEY (cedula) 
        REFERENCES CLIENTE(cedula)
        ON DELETE NO ACTION 
        ON UPDATE CASCADE
);
GO

-- MOVIMIENTO
CREATE TABLE MOVIMIENTO (
    codigoMovimiento INT IDENTITY(1,1) PRIMARY KEY,
    numCuenta VARCHAR(8) NOT NULL,
    tipo CHAR(3) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    CONSTRAINT FK_MOVIMIENTO_CUENTA FOREIGN KEY (numCuenta) 
        REFERENCES CUENTA(numCuenta)
        ON DELETE NO ACTION 
        ON UPDATE CASCADE
);
GO

-- CRÉDITO
CREATE TABLE CREDITO (
    idCredito INT IDENTITY(1,1) PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL,
    montoSolicitado DECIMAL(12,2) NOT NULL,
    montoAprobado DECIMAL(12,2),
    plazoMeses INT NOT NULL,
    tasaAnual DECIMAL(5,2) NOT NULL,
    cuotaFija DECIMAL(12,2) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
        CHECK (estado IN ('ACTIVO','CANCELADO')),
    fechaSolicitud DATE NOT NULL DEFAULT (CAST(GETDATE() AS DATE)),
    fechaAprobacion DATE,
    CONSTRAINT FK_CREDITO_CLIENTE FOREIGN KEY (cedula) 
        REFERENCES CLIENTE(cedula)
        ON DELETE NO ACTION 
        ON UPDATE CASCADE
);
GO

-- AMORTIZACIÓN
CREATE TABLE AMORTIZACION (
    idAmortizacion INT IDENTITY(1,1) PRIMARY KEY,
    idCredito INT NOT NULL,
    numeroCuota INT NOT NULL,
    valorCuota DECIMAL(12,2) NOT NULL,
    interesPagado DECIMAL(12,2) NOT NULL,
    capitalPagado DECIMAL(12,2) NOT NULL,
    saldo DECIMAL(12,2) NOT NULL,
    CONSTRAINT FK_AMORTIZACION_CREDITO FOREIGN KEY (idCredito) 
        REFERENCES CREDITO(idCredito)
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);
GO