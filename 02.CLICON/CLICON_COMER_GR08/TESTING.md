# 🧪 Guía de Testing - Cliente Consola

## ✅ Pre-requisitos

Antes de ejecutar el cliente, asegúrese de que:

1. ✅ El servidor `ws_comercializadora_gr08` esté ejecutándose en `http://localhost:8080`
2. ✅ Java 17 o superior esté instalado
3. ✅ Maven esté configurado correctamente

### Verificar servidor activo:
```bash
curl http://localhost:8080/ws_comercializadora_gr08/api/auth/health
```

## 🎯 Casos de Prueba

### 1️⃣ TEST: Pantalla de Inicio
**Objetivo**: Verificar que la pantalla de inicio se muestra correctamente

**Pasos**:
1. Ejecutar `run.bat` o `mvn exec:java`
2. Observar la pantalla de inicio

**Resultado Esperado**:
- ✅ Logo ASCII "ELECTRO" visible
- ✅ Título "COMERCIALIZADORA MONSTER"
- ✅ Subtítulo "Sistema de Gestión de Ventas"
- ✅ Colores ANSI aplicados correctamente
- ✅ Formulario de login visible

---

### 2️⃣ TEST: Login Exitoso - Usuario ADMIN
**Objetivo**: Verificar autenticación exitosa con rol ADMIN

**Datos de Entrada**:
```
Usuario: admin
Contraseña: admin
```

**Resultado Esperado**:
- ✅ Mensaje "¡Bienvenido admin!"
- ✅ Mensaje "Rol: ADMIN"
- ✅ Redirección al menú CRUD de electrodomésticos
- ✅ Opciones del menú ADMIN visibles:
  - 1. Listar todos los electrodomésticos
  - 2. Buscar electrodoméstico por ID
  - 3. Crear nuevo electrodoméstico
  - 4. Actualizar electrodoméstico
  - 5. Eliminar electrodoméstico
  - 0. Cerrar sesión

---

### 3️⃣ TEST: Login Exitoso - Usuario MONSTER
**Objetivo**: Verificar autenticación exitosa con rol USER (MONSTER)

**Datos de Entrada**:
```
Usuario: MONSTER
Contraseña: MONSTER9
```

**Resultado Esperado**:
- ✅ Mensaje "¡Bienvenido MONSTER!"
- ✅ Mensaje "Rol: USER"
- ✅ Redirección al menú de facturación
- ✅ Mensaje de bienvenida al sistema de facturación
- ✅ Opciones del menú MONSTER visibles:
  - 1. Consultar catálogo de electrodomésticos
  - 2. Registrar nueva venta (Próximamente)
  - 3. Consultar facturas (Próximamente)
  - 4. Reportes de ventas (Próximamente)
  - 0. Cerrar sesión

---

### 4️⃣ TEST: Login Fallido - Credenciales Incorrectas
**Objetivo**: Verificar manejo de credenciales incorrectas

**Datos de Entrada**:
```
Usuario: admin
Contraseña: incorrecta
```

**Resultado Esperado**:
- ✅ Mensaje de error en rojo: "Error: Contraseña incorrecta"
- ✅ Prompt: "¿Desea intentar nuevamente? (S/N):"
- ✅ Si responde 'S', vuelve al login
- ✅ Si responde 'N', cierra la aplicación

---

### 5️⃣ TEST: CRUD - Listar Electrodomésticos (ADMIN)
**Objetivo**: Verificar listado de electrodomésticos

**Pasos**:
1. Login como `admin`
2. Seleccionar opción `1` (Listar todos)

**Resultado Esperado**:
- ✅ Lista de electrodomésticos formateada
- ✅ Cada línea muestra: ID | Nombre | Precio
- ✅ Total de electrodomésticos al final
- ✅ Mensaje "Presione ENTER para continuar..."

**Ejemplo de salida**:
```
──────────────────────────────────────────────────────────────────────
  ID: 1   | Refrigeradora Samsung 420L              | $899.99
  ID: 2   | Lavadora LG 18kg                        | $699.99
  ID: 3   | Televisor LED 55 pulgadas               | $1299.99
──────────────────────────────────────────────────────────────────────
ℹ Total: 3 electrodomésticos
```

---

### 6️⃣ TEST: CRUD - Buscar por ID (ADMIN)
**Objetivo**: Verificar búsqueda de electrodoméstico específico

**Pasos**:
1. Login como `admin`
2. Seleccionar opción `2` (Buscar por ID)
3. Ingresar ID: `1`

**Resultado Esperado**:
- ✅ Datos del electrodoméstico con ID 1
- ✅ Formato: ID | Nombre | Precio

**Test de Error**:
- Ingresar ID inexistente (ej: `999`)
- ✅ Mensaje de error: "Error: ..."

---

### 7️⃣ TEST: CRUD - Crear Electrodoméstico (ADMIN)
**Objetivo**: Verificar creación de nuevo electrodoméstico

**Pasos**:
1. Login como `admin`
2. Seleccionar opción `3` (Crear nuevo)
3. Ingresar datos:
   ```
   Nombre: Licuadora Oster 600W
   Precio: 89.99
   ```

**Resultado Esperado**:
- ✅ Mensaje verde: "Electrodoméstico creado exitosamente!"
- ✅ Datos del nuevo electrodoméstico mostrados
- ✅ ID asignado automáticamente

---

### 8️⃣ TEST: CRUD - Actualizar Electrodoméstico (ADMIN)
**Objetivo**: Verificar actualización de electrodoméstico existente

**Pasos**:
1. Login como `admin`
2. Seleccionar opción `4` (Actualizar)
3. Ingresar ID existente (ej: `1`)
4. Ver datos actuales
5. Ingresar nuevos datos:
   ```
   Nuevo nombre: Refrigeradora Samsung 420L (Actualizado)
   Nuevo precio: 949.99
   ```

**Resultado Esperado**:
- ✅ Mensaje "Datos actuales:" con información previa
- ✅ Mensaje verde: "Electrodoméstico actualizado exitosamente!"
- ✅ Datos actualizados mostrados

---

### 9️⃣ TEST: CRUD - Eliminar Electrodoméstico (ADMIN)
**Objetivo**: Verificar eliminación de electrodoméstico

**Pasos**:
1. Login como `admin`
2. Seleccionar opción `5` (Eliminar)
3. Ingresar ID a eliminar
4. Confirmar con `S`

**Resultado Esperado**:
- ✅ Mensaje amarillo: "Se eliminará el siguiente electrodoméstico:"
- ✅ Datos del electrodoméstico a eliminar
- ✅ Prompt de confirmación: "¿Está seguro? (S/N):"
- ✅ Si `S`: Mensaje verde "Electrodoméstico eliminado exitosamente!"
- ✅ Si `N`: Mensaje "Operación cancelada."

**Test de Error**:
- Intentar eliminar electrodoméstico con facturas asociadas
- ✅ Mensaje de error apropiado

---

### 🔟 TEST: Cerrar Sesión
**Objetivo**: Verificar cierre de sesión correcto

**Pasos**:
1. Login con cualquier usuario
2. Seleccionar opción `0` (Cerrar sesión)

**Resultado Esperado**:
- ✅ Mensaje "Cerrando sesión..."
- ✅ Vuelta a la pantalla de login
- ✅ Token de autenticación eliminado

---

### 1️⃣1️⃣ TEST: Manejo de Errores de Conexión
**Objetivo**: Verificar comportamiento cuando el servidor no está disponible

**Pasos**:
1. Detener el servidor `ws_comercializadora_gr08`
2. Intentar login

**Resultado Esperado**:
- ✅ Mensaje de error en rojo: "Error de conexión: ..."
- ✅ Aplicación no se cierra abruptamente
- ✅ Opción de reintentar

---

### 1️⃣2️⃣ TEST: Validación de Entrada
**Objetivo**: Verificar validación de datos de entrada

**Casos de Prueba**:

**a) Entrada no numérica en menú**:
- Ingresar texto en lugar de número
- ✅ Mensaje: "Por favor ingrese un número válido."

**b) Precio inválido al crear electrodoméstico**:
- Ingresar texto en campo de precio
- ✅ Mensaje: "Por favor ingrese un número válido."

**c) Campos vacíos**:
- Dejar campos en blanco
- ✅ Manejo apropiado (puede aceptar o rechazar según lógica)

---

## 🎨 TEST: Elementos Visuales

### Colores ANSI
- ✅ Verde para mensajes de éxito (✓)
- ✅ Rojo para mensajes de error (✗)
- ✅ Azul para mensajes informativos (ℹ)
- ✅ Amarillo para advertencias (⚠)
- ✅ Cyan para encabezados y bordes

### Diseño
- ✅ Pantalla se limpia entre navegaciones
- ✅ Encabezados con bordes decorativos
- ✅ Líneas separadoras visibles
- ✅ Texto centrado en encabezados

---

## 📊 Matriz de Pruebas

| # | Caso de Prueba | Estado | Notas |
|---|----------------|--------|-------|
| 1 | Pantalla de inicio | ⬜ | |
| 2 | Login ADMIN | ⬜ | |
| 3 | Login MONSTER | ⬜ | |
| 4 | Login fallido | ⬜ | |
| 5 | Listar electrodomésticos | ⬜ | |
| 6 | Buscar por ID | ⬜ | |
| 7 | Crear electrodoméstico | ⬜ | |
| 8 | Actualizar electrodoméstico | ⬜ | |
| 9 | Eliminar electrodoméstico | ⬜ | |
| 10 | Cerrar sesión | ⬜ | |
| 11 | Error de conexión | ⬜ | |
| 12 | Validación de entrada | ⬜ | |

**Leyenda**: ⬜ Pendiente | ✅ Pasó | ❌ Falló

---

## 🐛 Reporte de Bugs

Si encuentra algún problema, documente:

1. **Descripción**: ¿Qué sucedió?
2. **Pasos para reproducir**: ¿Cómo llegó al error?
3. **Resultado esperado**: ¿Qué debería haber pasado?
4. **Resultado actual**: ¿Qué pasó realmente?
5. **Logs/Screenshots**: Evidencia del error

---

## ✅ Checklist Final

Antes de considerar el testing completo:

- [ ] Todos los casos de prueba ejecutados
- [ ] Matriz de pruebas completada
- [ ] Bugs documentados (si existen)
- [ ] Funcionalidades ADMIN verificadas
- [ ] Funcionalidades MONSTER verificadas
- [ ] Manejo de errores validado
- [ ] Interfaz visual correcta
- [ ] Documentación actualizada
