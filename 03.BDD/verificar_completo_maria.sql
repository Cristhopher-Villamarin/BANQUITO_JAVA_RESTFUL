-- VERIFICACIÓN COMPLETA DE MARÍA JOSÉ ANDRADE (1050423282)
-- Todos los movimientos (depósitos y retiros) últimos 3 meses

SELECT 
    m.fecha, 
    m.tipo, 
    m.valor,
    CASE 
        WHEN m.tipo = 'DEP' THEN 'DEPOSITO'
        WHEN m.tipo = 'RET' THEN 'RETIRO'
        ELSE m.tipo
    END as categoria,
    CASE 
        WHEN m.tipo = 'DEP' THEN '+'
        WHEN m.tipo = 'RET' THEN '-'
        ELSE '?'
    END as signo
FROM MOVIMIENTO m 
JOIN CUENTA c ON m.numCuenta = c.numCuenta 
WHERE c.cedula = '1050423282' 
AND m.tipo IN ('DEP', 'RET') 
AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
ORDER BY m.fecha, m.tipo;
