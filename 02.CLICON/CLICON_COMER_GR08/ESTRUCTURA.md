# 📁 Estructura del Proyecto - Cliente Consola

## 🎯 Arquitectura MVC Implementada

```
CLICON_COMER_GR08/
│
├── 📄 pom.xml                          # Configuración Maven con dependencias
├── 📄 README.md                        # Documentación del proyecto
├── 📄 run.bat                          # Script de ejecución rápida
│
└── src/main/java/ec/edu/monster/
    │
    ├── 🚀 Main.java                    # Punto de entrada de la aplicación
    │
    ├── 📦 model/                       # MODELO - DTOs y entidades
    │   ├── LoginRequest.java          # DTO para petición de login
    │   ├── LoginResponse.java         # DTO para respuesta de login
    │   ├── Electrodomestico.java      # Entidad Electrodoméstico
    │   └── ElectrodomesticoRequest.java # DTO para crear/actualizar
    │
    ├── 🎨 view/                        # VISTA - Interfaz de usuario
    │   └── ConsoleUI.java              # Utilidades de UI con colores ANSI
    │
    ├── 🎮 controller/                  # CONTROLADOR - Lógica de negocio
    │   ├── LoginController.java       # Gestión de autenticación
    │   ├── ElectrodomesticoController.java # CRUD de electrodomésticos (ADMIN)
    │   └── MonsterController.java     # Menú de facturación (MONSTER)
    │
    ├── 🔧 service/                     # SERVICIO - Capa de negocio
    │   ├── AuthService.java           # Servicio de autenticación
    │   └── ElectrodomesticoService.java # Servicio de electrodomésticos
    │
    └── 🗄️ db/                          # BASE DE DATOS - Conexión HTTP
        └── HttpClientUtil.java         # Cliente HTTP con Gson

```

## 🔄 Flujo de la Aplicación

```
┌─────────────────────────────────────────────────────────────┐
│                      INICIO APLICACIÓN                       │
│                         Main.java                            │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                   PANTALLA DE INICIO                         │
│              (Diseño estético minimalista)                   │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                    PANTALLA DE LOGIN                         │
│                  LoginController.java                        │
│                          │                                   │
│                          ▼                                   │
│              AuthService.login(user, pass)                   │
│                          │                                   │
│                          ▼                                   │
│         POST /api/auth/login (HttpClientUtil)                │
└──────────────────────┬──────────────────────────────────────┘
                       │
           ┌───────────┴───────────┐
           │                       │
           ▼                       ▼
    ┌─────────────┐         ┌─────────────┐
    │  ROL: ADMIN │         │ ROL: MONSTER│
    └──────┬──────┘         └──────┬──────┘
           │                       │
           ▼                       ▼
┌──────────────────────┐  ┌──────────────────────┐
│ ElectrodomesticoCtrl │  │   MonsterController  │
│                      │  │                      │
│ ✓ Listar            │  │ ✓ Bienvenida         │
│ ✓ Buscar            │  │ ✓ Menú Facturación   │
│ ✓ Crear             │  │ ⏳ Ventas (próximo)  │
│ ✓ Actualizar        │  │ ⏳ Facturas (próx.)  │
│ ✓ Eliminar          │  │ ⏳ Reportes (próx.)  │
└──────────────────────┘  └──────────────────────┘
```

## 🎨 Componentes de Vista (ConsoleUI)

### Características Visuales:
- ✅ **Colores ANSI**: Interfaz colorida y profesional
- ✅ **ASCII Art**: Logo y diseños estéticos
- ✅ **Mensajes tipificados**:
  - 🟢 Verde → Éxito
  - 🔴 Rojo → Error
  - 🔵 Azul → Información
  - 🟡 Amarillo → Advertencia
- ✅ **Limpieza de pantalla**: Navegación fluida
- ✅ **Validación de entrada**: Manejo robusto de errores

## 🔐 Sistema de Autenticación

### Credenciales:
```
ADMIN:
  Usuario: admin
  Password: admin
  Permisos: CRUD completo de electrodomésticos

MONSTER (USER):
  Usuario: MONSTER
  Password: MONSTER9
  Permisos: Sistema de facturación
```

### Flujo de Autenticación:
1. Usuario ingresa credenciales
2. `LoginController` → `AuthService.login()`
3. `AuthService` → `HttpClientUtil.post("/auth/login")`
4. Servidor valida y retorna `LoginResponse` con token
5. Token se guarda en `HttpClientUtil` para peticiones futuras
6. Redirección según rol (ADMIN o MONSTER)

## 🌐 Endpoints Consumidos

### Autenticación
```
POST /api/auth/login
  Body: { username, password }
  Response: { autenticado, mensaje, username, rol, token, timestamp }
```

### Electrodomésticos (Solo ADMIN)
```
GET    /api/electrodomesticos          → Listar todos
GET    /api/electrodomesticos/{id}     → Buscar por ID
POST   /api/electrodomesticos          → Crear nuevo
PUT    /api/electrodomesticos/{id}     → Actualizar
DELETE /api/electrodomesticos/{id}     → Eliminar
```

## 📊 Diagrama de Clases Simplificado

```
┌─────────────────┐
│      Main       │
└────────┬────────┘
         │ usa
         ▼
┌─────────────────────────────────────────┐
│           Controllers                    │
│  ┌──────────────────────────────────┐  │
│  │ LoginController                   │  │
│  │ ElectrodomesticoController        │  │
│  │ MonsterController                 │  │
│  └──────────┬───────────────────────┘  │
└─────────────┼───────────────────────────┘
              │ usa
              ▼
┌─────────────────────────────────────────┐
│            Services                      │
│  ┌──────────────────────────────────┐  │
│  │ AuthService                       │  │
│  │ ElectrodomesticoService           │  │
│  └──────────┬───────────────────────┘  │
└─────────────┼───────────────────────────┘
              │ usa
              ▼
┌─────────────────────────────────────────┐
│          HttpClientUtil                  │
│  ┌──────────────────────────────────┐  │
│  │ post(), get(), put(), delete()   │  │
│  │ Gestión de tokens                │  │
│  └──────────────────────────────────┘  │
└─────────────────────────────────────────┘
              │ consume
              ▼
┌─────────────────────────────────────────┐
│    ws_comercializadora_gr08 (REST API)  │
│         http://localhost:8080            │
└─────────────────────────────────────────┘
```

## 🚀 Ejecución

### Opción 1: Script Batch (Windows)
```bash
run.bat
```

### Opción 2: Maven
```bash
mvn clean compile
mvn exec:java
```

### Opción 3: JAR
```bash
mvn clean package
java -jar target/CLICON_COMER_GR08-1.0-SNAPSHOT.jar
```

## ✅ Checklist de Implementación

- [x] Arquitectura MVC completa
- [x] Paquete `ec.edu.monster` en todos los componentes
- [x] Pantalla de inicio estética y minimalista
- [x] Sistema de login funcional
- [x] Redirección por roles (ADMIN/MONSTER)
- [x] CRUD completo para ADMIN
- [x] Menú de bienvenida para MONSTER
- [x] Consumo de endpoints REST
- [x] Manejo de tokens de autenticación
- [x] Interfaz colorida con ANSI
- [x] Validación de entrada de usuario
- [x] Manejo de errores HTTP
- [x] Documentación completa

## 📝 Notas Técnicas

- **Java Version**: 17
- **Build Tool**: Maven
- **HTTP Client**: Apache HttpClient 5.2.1
- **JSON Parser**: Gson 2.10.1
- **Arquitectura**: MVC (Modelo-Vista-Controlador)
- **Servidor**: ws_comercializadora_gr08 @ localhost:8080
