-- =============================================
-- CORRECCIÓN DE TRANSFERENCIAS (TRF → TRD/TRC)
-- =============================================

-- Actualizar transferencias de salida (débito)
UPDATE MOVIMIENTO 
SET tipo = 'TRD' 
WHERE tipo = 'TRF' 
AND numCuenta IN (
    SELECT numCuenta FROM CUENTA WHERE cedula = '1002410692'
)
AND fecha IN ('2025-09-28');

-- Actualizar transferencias de entrada (crédito)  
UPDATE MOVIMIENTO 
SET tipo = 'TRC' 
WHERE tipo = 'TRF' 
AND numCuenta IN (
    SELECT numCuenta FROM CUENTA WHERE cedula = '1002410692'
)
AND fecha IN ('2025-10-27');

-- Verificar cambios
SELECT * FROM MOVIMIENTO 
WHERE numCuenta = '20412345' 
AND tipo IN ('TRD', 'TRC')
ORDER BY fecha;
