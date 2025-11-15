# GUÍA DE TESTING ACTUALIZADA - SERVICIO BANQUITO

## Resumen
Esta guía contiene los casos de prueba actualizados basados en los datos reales de la base de datos para el servicio de evaluación de crédito de Banquito.

## Reglas de Negocio
1. El solicitante debe ser cliente del banco
2. Si es casado y menor a 25 años → NO es sujeto de crédito
3. Si ya tiene crédito activo → NO es sujeto de crédito  
4. Si no tiene depósitos en el último mes → NO es sujeto de crédito
5. Si cumple las condiciones anteriores → SÍ es sujeto de crédito
6. El crédito se aprueba si el monto solicitado ≤ monto máximo autorizado

## Casos de Prueba - Endpoint: `/api/creditos/evaluar`

### Caso 1: María José Andrade Calderón (APROBADO)
**Datos:**
- Cédula: 1050423282
- Edad: 34 años
- Estado Civil: Casada (C)
- Depósitos recientes: SÍ (Octubre 2025)
- Crédito previo: Ninguno

**JSON Request:**
```json
{
  "cedula": "1050423282",
  "montoElectrodomestico": 1000.00,
  "plazoMeses": 12
}
```

**JSON Response Esperado:**
```json
{
  "sujetoCredito": true,
  "creditoAprobado": true,
  "mensaje": "Crédito aprobado",
  "montoMaximoCredito": 2250.00,
  "cuotaMensual": 203.52,
  "idCredito": 3,
  "tablaAmortizacion": [...]
}
```

---

### Caso 2: Carlos Alberto Moncayo Rivadeneira (RECHAZADO)
**Datos:**
- Cédula: 1752244770
- Edad: 26 años
- Estado Civil: Soltero (S)
- Depósitos recientes: SÍ (Octubre 2025)
- Crédito previo: PENDIENTE (2025-11-10)

**JSON Request:**
```json
{
  "cedula": "1752244770",
  "montoElectrodomestico": 1000.00,
  "plazoMeses": 12
}
```

**JSON Response Esperado:**
```json
{
  "sujetoCredito": false,
  "creditoAprobado": false,
  "mensaje": "El cliente ya tiene un crédito activo"
}
```

---

### Caso 3: Ana Cristina Vallejo Torres (APROBADO)
**Datos:**
- Cédula: 1752244887
- Edad: 37 años
- Estado Civil: Casada (C)
- Depósitos recientes: SÍ (Octubre 2025)
- Crédito previo: Ninguno

**JSON Request:**
```json
{
  "cedula": "1752244887",
  "montoElectrodomestico": 1000.00,
  "plazoMeses": 12
}
```

**JSON Response Esperado:**
```json
{
  "sujetoCredito": true,
  "creditoAprobado": true,
  "mensaje": "Crédito aprobado",
  "montoMaximoCredito": 4250.00,
  "cuotaMensual": 203.52,
  "idCredito": 4,
  "tablaAmortizacion": [...]
}
```

---

### Caso 4: Jorge Luis Santillán Morales (APROBADO)
**Datos:**
- Cédula: 1002410692
- Edad: 24 años
- Estado Civil: Soltero (S)
- Depósitos recientes: SÍ (Octubre 2025)
- Crédito previo: RECHAZADO (2025-10-15)

**JSON Request:**
```json
{
  "cedula": "1002410692",
  "montoElectrodomestico": 1000.00,
  "plazoMeses": 12
}
```

**JSON Response Esperado:**
```json
{
  "sujetoCredito": true,
  "creditoAprobado": true,
  "mensaje": "Crédito aprobado",
  "montoMaximoCredito": 400.00,
  "cuotaMensual": 203.52,
  "idCredito": 5,
  "tablaAmortizacion": [...]
}
```

---

### Caso 5: Patricia Isabel Cevallos Benítez (RECHAZADO)
**Datos:**
- Cédula: 1000846822
- Edad: 38 años
- Estado Civil: Divorciada (D)
- Depósitos recientes: SÍ (Octubre 2025)
- Crédito previo: ACTIVO (ID 2)

**JSON Request:**
```json
{
  "cedula": "1000846822",
  "montoElectrodomestico": 1000.00,
  "plazoMeses": 12
}
```

**JSON Response Esperado:**
```json
{
  "sujetoCredito": false,
  "creditoAprobado": false,
  "mensaje": "El cliente ya tiene un crédito activo"
}
```

---

## Casos de Prueba - Endpoint: `/api/ventas/procesar` (Comercializadora)

### Caso 1: María José (VENTA EXITOSA)
```json
{
  "cedulaCliente": "1050423282",
  "nombreCliente": "María José Andrade Calderón",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [{"idElectrodomestico": "1", "cantidad": 1}]
}
```

**Response Esperado:**
```json
{
  "ventaExitosa": true,
  "mensaje": "Venta procesada exitosamente",
  "idFactura": 1,
  "subtotal": 1200.00,
  "descuento": 0.00,
  "total": 1200.00,
  "formaPago": "CREDITO_DIRECTO",
  "estadoFactura": "APROBADA"
}
```

### Caso 2: Carlos (VENTA FALLIDA - CRÉDITO RECHAZADO)
```json
{
  "cedulaCliente": "1752244770",
  "nombreCliente": "Carlos Alberto Moncayo Rivadeneira",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [{"idElectrodomestico": "1", "cantidad": 1}]
}
```

**Response Esperado:**
```json
{
  "ventaExitosa": false,
  "mensaje": "Crédito no aprobado por el banco Banquito"
}
```

### Caso 3: Patricia (VENTA FALLIDA - CRÉDITO RECHAZADO)
```json
{
  "cedulaCliente": "1000846822",
  "nombreCliente": "Patricia Isabel Cevallos Benítez",
  "formaPago": "CREDITO_DIRECTO",
  "detalles": [{"idElectrodomestico": "1", "cantidad": 1}]
}
```

**Response Esperado:**
```json
{
  "ventaExitosa": false,
  "mensaje": "Crédito no aprobado por el banco Banquito"
}
```

## Comandos de Prueba

### Pruebas Directas a Banquito:
```bash
# María José (Aprobado)
curl -X POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1050423282","montoElectrodomestico":1000.00,"plazoMeses":12}'

# Carlos (Rechazado)
curl -X POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1752244770","montoElectrodomestico":1000.00,"plazoMeses":12}'

# Ana Cristina (Aprobado)
curl -X POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1752244887","montoElectrodomestico":1000.00,"plazoMeses":12}'

# Jorge (Aprobado)
curl -X POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1002410692","montoElectrodomestico":1000.00,"plazoMeses":12}'

# Patricia (Rechazado)
curl -X POST http://localhost:8080/ws_banquito_gr08/api/creditos/evaluar \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1000846822","montoElectrodomestico":1000.00,"plazoMeses":12}'
```

### Pruebas a Comercializadora:
```bash
# María José (Venta Exitosa)
curl -X POST http://localhost:8080/ws_comercializadora_gr08/api/ventas/procesar \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"1050423282","nombreCliente":"María José Andrade Calderón","formaPago":"CREDITO_DIRECTO","detalles":[{"idElectrodomestico":"1","cantidad":1}]}'

# Carlos (Venta Fallida)
curl -X POST http://localhost:8080/ws_comercializadora_gr08/api/ventas/procesar \
  -H "Content-Type: application/json" \
  -d '{"cedulaCliente":"1752244770","nombreCliente":"Carlos Alberto Moncayo Rivadeneira","formaPago":"CREDITO_DIRECTO","detalles":[{"idElectrodomestico":"1","cantidad":1}]}'
```

## Resumen de Resultados Esperados

**CRÉDITOS APROBADOS (3):**
- María José Andrade (1050423282)
- Ana Cristina Vallejo (1752244887)
- Jorge Luis Santillán (1002410692)

**CRÉDITOS RECHAZADOS (2):**
- Carlos Moncayo (1752244770) - tiene crédito pendiente
- Patricia Cevallos (1000846822) - tiene crédito activo

## Notas Importantes
1. El error de parseo JSON debe estar resuelto antes de ejecutar estas pruebas
2. Los montos máximos de crédito se calculan basados en el saldo de cuentas y depósitos recientes
3. Los IDs de crédito nuevos comenzarán desde 3 (ya existen 2 créditos en la base de datos)
4. Las fechas de prueba asumen que la fecha actual es posterior al 30 de octubre de 2025
