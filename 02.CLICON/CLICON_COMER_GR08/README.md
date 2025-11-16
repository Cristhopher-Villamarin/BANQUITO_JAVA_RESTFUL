# Cliente Consola - Comercializadora Monster

Cliente de consola Java que consume los servicios REST de la Comercializadora Monster, implementado con arquitectura MVC (Modelo-Vista-Controlador).

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)**:

```
ec.edu.monster/
├── Main.java                    # Punto de entrada de la aplicación
├── model/                       # Modelos (DTOs)
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── Electrodomestico.java
│   └── ElectrodomesticoRequest.java
├── view/                        # Vistas (UI de consola)
│   └── ConsoleUI.java
├── controller/                  # Controladores (lógica de negocio)
│   ├── LoginController.java
│   ├── ElectrodomesticoController.java
│   └── MonsterController.java
├── service/                     # Servicios (comunicación HTTP)
│   ├── AuthService.java
│   └── ElectrodomesticoService.java
└── db/                          # Utilidades de conexión
    └── HttpClientUtil.java
```

## 🚀 Características

### Pantalla de Inicio
- Diseño estético y minimalista con ASCII art
- Colores ANSI para mejor experiencia visual
- Interfaz dinámica y atractiva

### Sistema de Autenticación
- Login con validación de credenciales
- Dos roles disponibles:
  - **ADMIN**: Acceso completo al CRUD de electrodomésticos
  - **MONSTER** (USER): Acceso al sistema de facturación

### Funcionalidades por Rol

#### ROL ADMIN
- ✅ Listar todos los electrodomésticos
- ✅ Buscar electrodoméstico por ID
- ✅ Crear nuevo electrodoméstico
- ✅ Actualizar electrodoméstico existente
- ✅ Eliminar electrodoméstico

#### ROL MONSTER (USER)
- ✅ Pantalla de bienvenida al sistema de facturación
- ✅ Consultar catálogo de electrodomésticos
- ✅ Registrar nueva venta (EFECTIVO y CRÉDITO DIRECTO)
  - Captura de datos del cliente
  - Selección de forma de pago
  - Agregado de múltiples productos
  - Confirmación y procesamiento de venta
  - Visualización de factura generada
- 🔄 Consultar facturas (próximamente)
- 🔄 Reportes de ventas (próximamente)

## 📋 Requisitos

- Java 17 o superior
- Maven 3.6+
- Servidor `ws_comercializadora_gr08` ejecutándose en `http://localhost:8080`

## 🔧 Dependencias

```xml
<dependencies>
    <!-- Gson para manejo de JSON -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- Apache HttpClient para peticiones HTTP -->
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>5.2.1</version>
    </dependency>
</dependencies>
```

## 🎮 Uso

### Opción 1: Script de ejecución (Recomendado)
```bash
ejecutar.bat
```

### Opción 2: Maven directo
```bash
mvn clean compile
mvn exec:java
```

### Opción 3: Compilar JAR
```bash
mvn clean package
java -jar target/CLICON_COMER_GR08-1.0-SNAPSHOT.jar
```

### Credenciales de Acceso

**Usuario ADMIN:**
- Usuario: `admin`
- Contraseña: `admin`

**Usuario MONSTER:**
- Usuario: `MONSTER`
- Contraseña: `MONSTER9`

## 🌐 Endpoints Consumidos

### Autenticación
- `POST /api/auth/login` - Iniciar sesión

### Electrodomésticos (ADMIN y MONSTER)
- `GET /api/electrodomesticos` - Listar todos
- `GET /api/electrodomesticos/{id}` - Buscar por ID
- `POST /api/electrodomesticos` - Crear nuevo (Solo ADMIN)
- `PUT /api/electrodomesticos/{id}` - Actualizar (Solo ADMIN)
- `DELETE /api/electrodomesticos/{id}` - Eliminar (Solo ADMIN)

### Ventas (MONSTER)
- `POST /api/ventas/procesar` - Procesar nueva venta
  - Soporta EFECTIVO (33% descuento automático)
  - Soporta CRÉDITO_DIRECTO (validación con Banquito)

## 🎨 Características de UI

- **Colores ANSI**: Interfaz colorida y profesional
- **Mensajes informativos**: 
  - ✓ Verde para éxito
  - ✗ Rojo para errores
  - ℹ Azul para información
  - ⚠ Amarillo para advertencias
- **Limpieza de pantalla**: Navegación fluida entre menús
- **Validación de entrada**: Manejo robusto de errores de usuario

## 📝 Notas Técnicas

- El token de autenticación se gestiona automáticamente
- Las peticiones HTTP incluyen manejo de errores
- Arquitectura desacoplada siguiendo principios SOLID
- Código limpio y bien documentado

## 🔮 Próximas Funcionalidades

- [x] Módulo completo de ventas para rol MONSTER
- [x] Integración con servicio de créditos del Banquito
- [x] Generación de facturas
- [ ] Consulta de facturas existentes
- [ ] Reportes y estadísticas
- [ ] Historial de transacciones

## 👥 Autores

Grupo 08 - Arquitectura de Software
- Comercializadora Monster
- Sistema de Facturación

---

**Versión:** 1.0-SNAPSHOT  
**Fecha:** Noviembre 2024
