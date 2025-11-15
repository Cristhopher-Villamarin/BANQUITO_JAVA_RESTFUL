-- DEBUG: Verificar datos de María José Andrade (1050423282)

-- 1. Verificar depósitos últimos 3 meses
SELECT 
    m.fecha, 
    m.tipo, 
    m.valor,
    'DEPOSITO' as categoria
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo = 'DEP' 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
ORDER BY m.fecha;

-- 2. Verificar retiros últimos 3 meses
SELECT 
    m.fecha, 
    m.tipo, 
    m.valor,
    'RETIRO' as categoria
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo = 'RET' 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
ORDER BY m.fecha;

-- 3. Calcular promedio depósitos
SELECT 
    COUNT(*) as total_depositos,
    SUM(m.valor) as suma_depositos,
    AVG(m.valor) as promedio_depositos
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo = 'DEP' 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH);

-- 4. Calcular promedio retiros
SELECT 
    COUNT(*) as total_retiros,
    SUM(m.valor) as suma_retiros,
    AVG(m.valor) as promedio_retiros
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo = 'RET' 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH);

-- 5. Cálculo manual del monto máximo
SELECT 
    AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END) as promedio_depositos,
    AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END) as promedio_retiros,
    (AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END) - AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END)) as diferencia,
    (AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END) - AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END)) * 0.60 as sesenta_porciento,
    ((AVG(CASE WHEN m.tipo = 'DEP' THEN m.valor END) - AVG(CASE WHEN m.tipo = 'RET' THEN m.valor END)) * 0.60) * 9 as monto_maximo
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo IN ('DEP', 'RET') 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH);
