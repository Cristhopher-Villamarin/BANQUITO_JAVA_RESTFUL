# 🧪 Guía Completa de Testing - Sistema Banquito & Comercializadora

## 📋 Datos de Prueba (Actualizados)

### Clientes del Banco Banquito:
| Cédula | Nombre | Estado Civil | Edad | Perfil | Saldo Cuenta | Crédito Previo |
|--------|--------|--------------|------|--------|--------------|----------------|
| 1050423282 | María José Andrade | Casado (C) | 34 | ✅ Cliente con depósitos | $4,500 | Ninguno |
| 1752244770 | Carlos Moncayo | Soltero (S) | 26 | ✅ Cliente con depósitos | $2,800 | PENDIENTE |
| 1752244887 | Ana Cristina Vallejo | Casado (C) | 37 | ✅ Cliente con depósitos | $8,500 | Ninguno |
| 1002410692 | Jorge Luis Santillán | Soltero (S) | 24 | ✅ Cliente con depósitos | $800 | RECHAZADO |
| 1000846822 | Patricia Cevallos | Divorciado (D) | 38 | ✅ Cliente con depósitos | $3,200 | ACTIVO |

### Electrodomésticos Disponibles:
| ID | Producto | Precio |
|----|----------|--------|
| 1 | Refrigeradora LG 18 pies | $1,299.99 |
| 2 | Lavadora Samsung 20 libras | $899.50 |
| 3 | Cocina Indurama 6 quemadores | $599.00 |
| 4 | Microondas Panasonic 1.2 pies | $249.99 |
| 5 | Televisor Sony 55 pulgadas 4K | $1,899.00 |
| 6 | Licuadora Oster 10 velocidades | $89.99 |
| 7 | Aspiradora Electrolux 2000W | $349.00 |
| 8 | Plancha Philips a vapor | $45.50 |
| 9 | Ventilador Samurai 20 pulgadas | $125.00 |
| 10 | Aire Acondicionado LG 12000 BTU | $799.99 |

---

## 🏦 BANQUITO - Endpoints de Testing

### 1️⃣ Health Check
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/health
```
**Respuesta Esperada:**
```json
{
  "status": "OK",
  "message": "Banquito Credit Service is running",
  "timestamp": "2025-11-15T00:00:00"
}
```

### 2️⃣ Verificar Sujeto de Crédito
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/{cedula}/sujeto-credito
```

#### ✅ Caso APROBADO - María José (Casada, 35 años, con depósitos)
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1050423282/sujeto-credito
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": true,
  "mensaje": "Cliente cumple requisitos para ser sujeto de crédito",
  "montoMaximoCredito": 8100.00
}
```

#### ✅ Caso APROBADO - Jorge (24 años, soltero, con depósitos recientes)
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1002410692/sujeto-credito
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": true,
  "mensaje": "Cliente cumple requisitos para ser sujeto de crédito",
  "montoMaximoCredito": 400.00
}
```
*Nota: Jorge es soltero con 24 años, por lo que la regla de edad >=25 para casados no aplica. Tiene depósitos en octubre (480.00 y 460.00), por lo que cumple requisitos.*

#### ❌ Caso RECHAZADO - Cliente sin depósitos recientes (simulado)
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/9999999999/sujeto-credito
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": false,
  "mensaje": "El solicitante no es cliente del banco"
}
```
*Nota: Este cliente no existe en la base de datos del banco.*

#### ❌ Caso RECHAZADO - Carlos (Tiene crédito PENDIENTE)
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1752244770/sujeto-credito
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": false,
  "mensaje": "El cliente ya tiene un crédito activo"
}
```
*Nota: Carlos tiene un crédito con estado 'PENDIENTE' desde 2025-11-10.*

#### ❌ Caso RECHAZADO - Patricia (Tiene crédito ACTIVO)
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1000846822/sujeto-credito
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": false,
  "mensaje": "El cliente ya tiene un crédito activo"
}
```

### 3️⃣ Obtener Monto Máximo de Crédito
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/{cedula}/monto-maximo
```

#### ✅ Ana Cristina (Alto perfil)
```bash
GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1752244887/monto-maximo
```
**Respuesta Esperada:**
```json
8100.00
```

### 4️⃣ Evaluación Completa de Crédito
```bash
POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar
Content-Type: application/json
```

#### ❌ Caso RECHAZADO - Carlos Moncayo (tiene crédito PENDIENTE)
```json
{
  "cedula": "1752244770",
  "montoElectrodomestico": 899.50,
  "plazoMeses": 12
}
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": false,
  "creditoAprobado": false,
  "mensaje": "El cliente ya tiene un crédito activo"
}
```

#### ❌ Caso RECHAZADO - Monto excede máximo
```json
{
  "cedula": "1002410692",
  "montoElectrodomestico": 500.00,
  "plazoMeses": 12
}
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": true,
  "mensaje": "Monto del electrodoméstico excede el crédito máximo autorizado",
  "montoMaximoCredito": 400.00,
  "creditoAprobado": false
}
```

#### ✅ Caso APROBADO - Ana Cristina (perfil alto)
```json
{
  "cedula": "1752244887",
  "montoElectrodomestico": 1899.00,
  "plazoMeses": 12
}
```
**Respuesta Esperada:**
```json
{
  "sujetoCredito": true,
  "mensaje": "Crédito aprobado",
  "montoMaximoCredito": 8100.00,
  "creditoAprobado": true,
  "cuotaMensual": 171.48,
  "idCredito": 3,
  "tablaAmortizacion": [
    {
      "numeroCuota": 1,
      "valorCuota": 171.48,
      "interesPagado": 25.32,
      "capitalPagado": 146.16,
      "saldo": 1752.84
    }
    // ... continuando hasta la cuota 12
  ]
}
```

---

## 🛍️ COMERCIALIZADORA - Endpoints de Testing

### 1️⃣ Health Check
```bash
GET http://localhost:8080/ws_comercializadora_gr08/api/ventas/health
```
**Respuesta Esperada:**
```json
{
  "status": "OK",
  "message": "Comercializadora Service is running",
  "timestamp": "2025-11-15T00:00:00"
}
```

### 2️⃣ Listar Electrodomésticos
```bash
GET http://localhost:8080/ws_comercializadora_gr08/api/ventas/electrodomesticos
```
**Respuesta Esperada:**
```json
[
  {
    "idElectrodomestico": 1,
    "nombre": "Refrigeradora LG 18 pies",
    "precioVenta": 1299.99
  },
  {
    "idElectrodomestico": 2,
    "nombre": "Lavadora Samsung 20 libras",
    "precioVenta": 899.50
  }
  // ... todos los electrodomésticos
]
```

### 3️⃣ Procesar Venta en EFECTIVO (33% descuento)
```bash
POST http://localhost:8080/ws_comercializadora_gr08/api/ventas/procesar
Content-Type: application/json
```

#### ✅ Compra en efectivo - María José
```json
{
  "cedulaCliente": "1050423282",
  "nombreCliente": "María José Andrade Calderón",
  "formaPago": "EFECTIVO",
  "detalles": [
    {
      "idElectrodomestico": 1,
      "cantidad": 1
    },
    {
      "idElectrodomestico": 6,
      "cantidad": 2
    }
  ]
}
```
**Respuesta Esperada:**
```json
{
  "ventaExitosa": true,
  "mensaje": "Venta procesada exitosamente",
  "idFactura": 1,
  "subtotal": 1479.97,
  "descuento": 488.39,
  "total": 991.58,
  "formaPago": "EFECTIVO",
  "estadoFactura": "PAGADA",
  "detalles": [
    {
      "idElectrodomestico": 1,
      "nombreElectrodomestico": "Refrigeradora LG 18 pies",
      "cantidad": 1,
      "precioUnitario": 1299.99,
      "subtotalLinea": 1299.99
    },
    {
      "idElectrodomestico": 6,
      "nombreElectrodomestico": "Licuadora Oster 10 velocidades",
      "cantidad": 2,
      "precioUnitario": 89.99,
      "subtotalLinea": 179.98
    }
  ]
}
```

### 4️⃣ Procesar Venta en CRÉDITO DIRECTO
```bash
POST http://localhost:8080/ws_comercializadora_gr08/api/ventas/procesar
Content-Type: application/json
```

#### ✅ Crédito APROBADO - Ana Cristina (perfil alto)
```json
{
  "cedulaCliente": "1752244887",
  "nombreCliente": "Ana Cristina Vallejo Torres",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [
    {
      "idElectrodomestico": 5,
      "cantidad": 1
    }
  ]
}
```
**Respuesta Esperada:**
```json
{
  "ventaExitosa": true,
  "mensaje": "Venta procesada exitosamente",
  "idFactura": 2,
  "subtotal": 1899.00,
  "descuento": 0.00,
  "total": 1899.00,
  "formaPago": "CREDITO_DIRECTO",
  "estadoFactura": "APROBADA",
  "detalles": [
    {
      "idElectrodomestico": 5,
      "nombreElectrodomestico": "Televisor Sony 55 pulgadas 4K",
      "cantidad": 1,
      "precioUnitario": 1899.00,
      "subtotalLinea": 1899.00
    }
  ]
}
```

#### ✅ Crédito APROBADO - Jorge (cumple requisitos)
```json
{
  "cedulaCliente": "1002410692",
  "nombreCliente": "Jorge Luis Santillán Morales",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [
    {
      "idElectrodomestico": 4,
      "cantidad": 1
    }
  ]
}
```
**Respuesta Esperada:**
```json
{
  "ventaExitosa": true,
  "mensaje": "Venta procesada exitosamente",
  "idFactura": 3,
  "subtotal": 249.99,
  "descuento": 0.00,
  "total": 249.99,
  "formaPago": "CREDITO_DIRECTO",
  "estadoFactura": "APROBADA",
  "detalles": [
    {
      "idElectrodomestico": 4,
      "nombreElectrodomestico": "Microondas Panasonic 1.2 pies",
      "cantidad": 1,
      "precioUnitario": 249.99,
      "subtotalLinea": 249.99
    }
  ]
}
```

#### ❌ Crédito RECHAZADO - Patricia (tiene crédito ACTIVO)
```json
{
  "cedulaCliente": "1000846822",
  "nombreCliente": "Patricia Isabel Cevallos Benítez",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [
    {
      "idElectrodomestico": 2,
      "cantidad": 1
    }
  ]
}
```
**Respuesta Esperada:**
```json
{
  "ventaExitosa": false,
  "mensaje": "Crédito no aprobado por el banco Banquito"
}
```

#### ❌ Crédito RECHAZADO - Carlos (tiene crédito PENDIENTE)
```json
{
  "cedulaCliente": "1752244770",
  "nombreCliente": "Carlos Alberto Moncayo Rivadeneira",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [
    {
      "idElectrodomestico": 3,
      "cantidad": 1
    }
  ]
}
```
**Respuesta Esperada:**
```json
{
  "ventaExitosa": false,
  "mensaje": "Crédito no aprobado por el banco Banquito"
}
```

---

## 🧮 Cálculos Matemáticos de Referencia

### Fórmula Cuota Fija:
```
Cuota = Valor / (1 - ((1+TasaPeriodo)^-NúmeroCuotas)) / TasaPeriodo
TasaPeriodo = 16%/12 = 0.013333...
```

### Ejemplo: $899.50 a 12 meses
```
TasaPeriodo = 0.16/12 = 0.013333
(1+TasaPeriodo)^-12 = (1.013333)^-12 = 0.851
Denominador = 1 - 0.851 = 0.149
Cuota = 899.50 / 0.149 / 0.013333 = $81.25
```

### Fórmula Monto Máximo:
```
((Promedio Depósitos – Promedio Retiros) * 60%) * 9
```

---

## 🚀 Comandos cURL para Testing

```bash
# Health Checks
curl -X GET http://localhost:8080/ws_banquito_gr08/api/creditos/health
curl -X GET http://localhost:8080/ws_comercializadora_gr08/api/ventas/health

# Verificar sujetos de crédito
curl -X GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1050423282/sujeto-credito
curl -X GET http://localhost:8080/ws_banquito_gr08/api/creditos/cliente/1002410692/sujeto-credito

# Evaluación de crédito
curl -X POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1752244770","montoElectrodomestico":899.50,"plazoMeses":12}'

# Venta en efectivo
curl -X POST http://localhost:8080/ws_comercializadora_gr08/api/ventas/procesar \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"1050423282","nombreCliente":"María José Andrade","formaPago":"EFECTIVO","detalles":[{"idElectrodomestico":1,"cantidad":1}]}'

# Venta crédito directo
curl -X POST http://localhost:8080/ws_comercializadora_gr08/api/ventas/procesar \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"1752244887","nombreCliente":"Ana Cristina Vallejo","formaPago":"CREDITO_DIRECTO","detalles":[{"idElectrodomestico":5,"cantidad":1}]}'
```

---

## ✅ Resultados Esperados (Actualizados)

1. **María José (1050423282)**: ✅ Sujeto crédito, monto máximo ~$4,050
2. **Carlos (1752244770)**: ❌ No sujeto crédito (tiene crédito PENDIENTE)
3. **Ana Cristina (1752244887)**: ✅ Sujeto crédito, monto máximo ~$8,100
4. **Jorge (1002410692)**: ✅ Sujeto crédito, monto máximo ~$400
5. **Patricia (1000846822)**: ❌ No sujeto crédito (crédito ACTIVO)

**Ventas Efectivo**: 33% descuento automático  
**Ventas Crédito**: Validación con Banquito antes de aprobar
