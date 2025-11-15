-- VERIFICACIÓN DE DATOS DE MARÍA JOSÉ ANDRADE (1050423282)
-- Ejecutar estas consultas para identificar la discrepancia

-- 1. Ver todos los movimientos de María José
SELECT 
    m.fecha, 
    m.tipo, 
    m.valor,
    CASE 
        WHEN m.tipo = 'DEP' THEN 'DEPOSITO'
        WHEN m.tipo = 'RET' THEN 'RETIRO'
        WHEN m.tipo = 'TRF' THEN 'TRANSFERENCIA'
        ELSE m.tipo
    END as categoria
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
ORDER BY m.fecha, m.tipo;

-- 2. Solo depósitos últimos 3 meses (lo que usa el sistema)
SELECT 
    COUNT(*) as total_depositos,
    SUM(m.valor) as suma_depositos,
    AVG(m.valor) as promedio_depositos
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo = 'DEP' 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH);

-- 3. Solo retiros últimos 3 meses (lo que usa el sistema)
SELECT 
    COUNT(*) as total_retiros,
    SUM(m.valor) as suma_retiros,
    AVG(m.valor) as promedio_retiros
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo = 'RET' 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH);

-- 4. Cálculo exacto del sistema (misma fórmula que Java)
SELECT 
    COALESCE(AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END), 0) as promedio_depositos,
    COALESCE(AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END), 0) as promedio_retiros,
    (COALESCE(AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END), 0) - COALESCE(AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END), 0)) as diferencia,
    (COALESCE(AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END), 0) - COALESCE(AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END), 0)) * 0.60 as sesenta_porciento,
    ROUND(((COALESCE(AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END), 0) - COALESCE(AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END), 0)) * 0.60) * 9, 2) as monto_maximo_sistema
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo IN ('DEP', 'RET') 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH);
