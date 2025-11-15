-- Conectar como root primero: mysql -u root -p

-- Dar todos los permisos al usuario MONSTER sobre ambas bases de datos
GRANT ALL PRIVILEGES ON banquito_credito.* TO 'MONSTER'@'%';
GRANT ALL PRIVILEGES ON comercializadora.* TO 'MONSTER'@'%';

-- Recargar los privilegios
FLUSH PRIVILEGES;

-- Verificar los permisos
SHOW GRANTS FOR 'MONSTER'@'%';

-- Salir
EXIT;
