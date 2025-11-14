# 🚀 GUÍA COMPLETA - PATRÓN ADAPTER EMPLEADOS

## 📋 REQUISITOS DEL PARCIAL IMPLEMENTADOS

✅ **Dos clases para consultar empleados:**
- `EmpleadoDB` - Consulta directa a base de datos
- `EmpleadoWS` - Consulta mediante Web Service REST

✅ **Firmas de métodos DISTINTAS:**
- `EmpleadoDB.buscarEmpleadoPorCodigo(String codigo)` → boolean
- `EmpleadoWS.getEmployeeByCode(int employeeCode)` → String JSON

✅ **Patrón Adapter implementado:**
- `AdapterDB` adapta EmpleadoDB
- `AdapterWS` adapta EmpleadoWS
- Ambos implementan `IEmpleadoAdapter`

✅ **Base de datos PostgreSQL en la nube:**
- Supabase (gratis)

---

## 🛠️ PASO 1: INSTALAR JDK

### Windows:
1. Descargar JDK 11+ desde: https://www.oracle.com/java/technologies/downloads/
2. O usar OpenJDK: https://adoptium.net/
3. Instalar y agregar a PATH

### Verificar instalación:
```cmd
java -version
javac -version
```

Deberías ver algo como: `java version "11.0.x"`

---

## 📦 PASO 2: DESCARGAR LIBRERÍAS

### 2.1 PostgreSQL JDBC Driver
- **URL**: https://jdbc.postgresql.org/download/
- **Link directo**: https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.1/postgresql-42.7.1.jar
- **Archivo**: `postgresql-42.7.1.jar`
- **Dónde guardarlo**: Carpeta `lib/`

### 2.2 Gson (para JSON)
- **URL**: https://github.com/google/gson
- **Link directo**: https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
- **Archivo**: `gson-2.10.1.jar`
- **Dónde guardarlo**: Carpeta `lib/`

---

## 📂 PASO 3: CREAR ESTRUCTURA DE CARPETAS

### Opción A: PowerShell (Windows)
```powershell
# Crear carpeta principal
mkdir patron-adapter-empleados
cd patron-adapter-empleados

# Crear subcarpetas
mkdir src\database, src\webservice, src\adapter, src\main, lib, bin

# Crear archivos Java
New-Item -ItemType File src\database\DatabaseConnection.java
New-Item -ItemType File src\database\EmpleadoDB.java
New-Item -ItemType File src\webservice\EmpleadoWS.java
New-Item -ItemType File src\webservice\EmpleadoWSResponse.java
New-Item -ItemType File src\adapter\IEmpleadoAdapter.java
New-Item -ItemType File src\adapter\EmpleadoUnificado.java
New-Item -ItemType File src\adapter\AdapterDB.java
New-Item -ItemType File src\adapter\AdapterWS.java
New-Item -ItemType File src\main\Main.java
```

### Opción B: Manualmente
Crea esta estructura:
```
patron-adapter-empleados/
├── src/
│   ├── database/
│   │   ├── DatabaseConnection.java
│   │   └── EmpleadoDB.java
│   ├── webservice/
│   │   ├── EmpleadoWS.java
│   │   └── EmpleadoWSResponse.java
│   ├── adapter/
│   │   ├── IEmpleadoAdapter.java
│   │   ├── EmpleadoUnificado.java
│   │   ├── AdapterDB.java
│   │   └── AdapterWS.java
│   └── main/
│       └── Main.java
├── lib/
│   ├── postgresql-42.7.1.jar
│   └── gson-2.10.1.jar
└── bin/
```

---

## 🗄️ PASO 4: CONFIGURAR SUPABASE

### 4.1 Crear cuenta
1. Ve a: https://supabase.com
2. Crea cuenta (gratis)
3. Crea un nuevo proyecto
4. Espera 2-3 minutos a que se inicialice

### 4.2 Obtener credenciales
1. En tu proyecto, ve a **Settings** (⚙️) → **Database**
2. En **Connection Info**, copia:
   - **Host**: `db.xxxxxx.supabase.co`
   - **Database**: `postgres`
   - **Port**: `5432`
   - **User**: `postgres.xxxxxx`
   - **Password**: [tu contraseña]

**Ejemplo de credenciales:**
```
Host: db.xyzabc123456.supabase.co
User: postgres.xyzabc123456
Password: MiPassword123!
```

### 4.3 Ejecutar SQL
1. Ve a **SQL Editor** en Supabase
2. Copia y pega el script SQL que te proporcioné (ver artifact "Script SQL para Supabase")
3. Haz clic en **RUN** o presiona `Ctrl+Enter`
4. Verifica que se crearon 10 empleados:
```sql
SELECT * FROM empleados;
```

### 4.4 Crear archivo `supabase.configs` (recomendado)

Puedes colocar las credenciales de Supabase en un archivo llamado `supabase.configs` en la raíz del proyecto. Esto permite que la aplicación cargue automáticamente la configuración al iniciar.

Ejemplo de archivo `supabase.configs` (ponlo en la carpeta raíz del proyecto `patron-adapter-empleados`):

```
#Supabase configuration (auto-generated)
SUPABASE_API_KEY=tu_api_key_aqui
SUPABASE_DATABASE_URL=postgresql://postgres.tu_proyecto:tu_password@db.tu_proyecto.supabase.co:5432/postgres
SUPABASE_PROJECT_URL=https://tu_proyecto.supabase.co
```

Notas:
- El archivo puede contener comentarios con `#`.
- La clase `DatabaseConnection` ya viene preparada para leer `supabase.configs` y extraer `SUPABASE_DATABASE_URL` (si existe) o usar variables de entorno si las prefieres.
- Si no quieres usar el archivo, también puedes definir las variables de entorno `SUPABASE_DATABASE_URL`, `SUPABASE_DB_USER` y `SUPABASE_DB_PASSWORD`.

---

## 💻 PASO 5: CONFIGURAR CÓDIGO

### 5.1 Copiar todo el código
Abre Visual Studio Code y copia cada archivo que te proporcioné en su ubicación correspondiente.

### 5.2 IMPORTANTE: Configurar DatabaseConnection.java
Abre `src/database/DatabaseConnection.java` y modifica estas líneas (18-23):

**ANTES:**
```java
private static final String URL = "jdbc:postgresql://db.TU_PROYECTO_ID.supabase.co:5432/postgres";
private static final String USER = "postgres.TU_PROYECTO_ID";
private static final String PASSWORD = "TU_PASSWORD_AQUI";
```

**DESPUÉS (con tus datos reales):**
```java
private static final String URL = "jdbc:postgresql://db.xyzabc123456.supabase.co:5432/postgres";
private static final String USER = "postgres.xyzabc123456";
private static final String PASSWORD = "MiPassword123!";
```

**⚠️ IMPORTANTE:** 
- Reemplaza `xyzabc123456` con tu ID real de Supabase
- Reemplaza `MiPassword123!` con tu contraseña real
- NO compartas estas credenciales

---

## ⚙️ PASO 6: COMPILAR EL PROYECTO

### Opción A: Línea de comandos (PowerShell/CMD)

```cmd
cd patron-adapter-empleados

javac -encoding UTF-8 -cp "lib/*" -d bin src/database/*.java src/webservice/*.java src/adapter/*.java src/main/*.java
```

Si ves errores, verifica:
- ✅ Los archivos .jar están en `lib/`
- ✅ La carpeta `bin/` existe
- ✅ Estás en la carpeta raíz del proyecto

### Opción B: Crear archivo compilar.bat

Crea un archivo `compilar.bat` en la raíz:

```batch
@echo off
echo ================================================
echo   COMPILANDO PATRON ADAPTER - EMPLEADOS
echo ================================================
if not exist bin mkdir bin
javac -encoding UTF-8 -cp "lib/*" -d bin src/database/*.java src/webservice/*.java src/adapter/*.java src/main/*.java
if %ERRORLEVEL% == 0 (
    echo.
    echo [OK] Compilacion exitosa
    echo [OK] Archivos .class generados en bin/
    echo.
) else (
    echo.
    echo [ERROR] Error en la compilacion
    echo [ERROR] Verifica que los .jar esten en lib/
    echo.
    pause
)
```

Ejecutar: `compilar.bat`

---

## ▶️ PASO 7: EJECUTAR LA APLICACIÓN

### Opción A: Línea de comandos

```cmd
java -cp "bin;lib/*" main.Main
```

**En Linux/Mac usa `:` en vez de `;`:**
```bash
java -cp "bin:lib/*" main.Main
```

### Opción B: Crear archivo ejecutar.bat

Crea un archivo `ejecutar.bat`:

```batch
@echo off
echo ================================================
echo   EJECUTANDO PATRON ADAPTER - EMPLEADOS
echo ================================================
echo.
java -cp "bin;lib/*" main.Main
echo.
echo ================================================
pause
```

Ejecutar: `ejecutar.bat`

### Opción C: Todo en uno (compilar_y_ejecutar.bat)

```batch
@echo off
echo ================================================
echo   COMPILAR Y EJECUTAR
echo ================================================
call compilar.bat
if %ERRORLEVEL% == 0 (
    echo.
    echo Presiona cualquier tecla para ejecutar...
    pause > nul
    call ejecutar.bat
)
```

---

## 🎮 PASO 8: USAR LA APLICACIÓN

Cuando la aplicación inicie, verás este menú:

```
╔═════════════════════════════════════════════════════════════════╗
║     SISTEMA DE CONSULTA DE EMPLEADOS - PATRÓN ADAPTER          ║
╠═════════════════════════════════════════════════════════════════╣
║  1. 📊 Consultar por Base de Datos (Método Directo)            ║
║  2. 🌐 Consultar por Web Service (Método REST)                 ║
║  3. ⚖️  Comparar ambos métodos                                  ║
║  4. 📖 Explicación del Patrón Adapter                          ║
║  5. 📋 Listar empleados disponibles                            ║
║  6. 🚪 Salir                                                    ║
╚═════════════════════════════════════════════════════════════════╝
```

### Ejemplos de uso:

**Opción 1 - Consulta por Base de Datos:**
```
Seleccione: 1
Código: EMP001
```
Verás información detallada del empleado desde la base de datos.

**Opción 2 - Consulta por Web Service:**
```
Seleccione: 2
Código: EMP002
```
Verás la misma información pero obtenida vía Web Service (con latencia simulada).

**Opción 3 - Comparar ambos métodos:**
```
Seleccione: 3
Código: EMP003
```
Ejecuta ambas consultas y compara tiempos y resultados.

**Opción 4 - Explicación del Patrón:**
Muestra una explicación detallada del patrón Adapter implementado.

**Opción 5 - Listar empleados:**
Muestra todos los códigos disponibles (EMP001 a EMP010).

---

## 🔧 SOLUCIÓN DE PROBLEMAS

### ❌ Error: "Driver not found"
**Problema:** No encuentra `org.postgresql.Driver`

**Solución:**
1. Verifica que `postgresql-42.7.1.jar` esté en `lib/`
2. Compila con: `javac -cp "lib/*" ...`
3. Ejecuta con: `java -cp "bin;lib/*" ...`

### ❌ Error: "Connection refused"
**Problema:** No puede conectar a Supabase

**Solución:**
1. Verifica credenciales en `DatabaseConnection.java`
2. Verifica que tu proyecto Supabase esté activo
3. Verifica conexión a internet
4. Revisa firewall (permite conexión a puerto 5432)

### ❌ Error: "Class not found: com.google.gson.Gson"
**Problema:** No encuentra la librería Gson

**Solución:**
1. Verifica que `gson-2.10.1.jar` esté en `lib/`
2. Recompila con las librerías

### ❌ Error: "javac no se reconoce"
**Problema:** Java no está en el PATH

**Solución:**
1. Reinstala JDK
2. Agrega JDK a PATH de Windows:
   - Variables de entorno → Path → Agregar: `C:\Program Files\Java\jdk-11\bin`

### ❌ Error de encoding
**Problema:** Caracteres especiales no se ven bien

**Solución:**
Agrega `-encoding UTF-8` al compilar:
```cmd
javac -encoding UTF-8 -cp "lib/*" ...
```

---

## 📊 CÓDIGOS DE EMPLEADOS DISPONIBLES

| Código | Nombre | Cargo |
|--------|--------|-------|
| EMP001 | Juan Pérez | Desarrollador Senior |
| EMP002 | María González | Analista de Datos |
| EMP003 | Carlos Rodríguez | Arquitecto de Software |
| EMP004 | Ana Martínez | DevOps Engineer |
| EMP005 | Luis Hernández | Product Manager |
| EMP006 | Sofia Torres | UX Designer |
| EMP007 | Diego Ramírez | Scrum Master |
| EMP008 | Laura Castro | Desarrolladora Full Stack |
| EMP009 | Miguel Vargas | Security Engineer |
| EMP010 | Patricia Morales | QA Lead |

---

## 🎯 EXPLICACIÓN DEL PATRÓN ADAPTER

### Problema:
Tenemos DOS clases incompatibles:

**EmpleadoDB:**
- Método: `buscarEmpleadoPorCodigo(String codigo)`
- Retorna: `boolean`
- Modifica estado interno

**EmpleadoWS:**
- Método: `getEmployeeByCode(int employeeCode)`
- Retorna: `String` (JSON)
- Nombres en inglés

### Solución:
Creamos una **interfaz común** (`IEmpleadoAdapter`) y dos **adaptadores**:

```
Cliente (Main)
    ↓ usa
IEmpleadoAdapter
    ↓ implementan
    ├→ AdapterDB → EmpleadoDB (Base de Datos)
    └→ AdapterWS → EmpleadoWS (Web Service)
```

### Ventajas:
✅ Una sola interfaz para múltiples fuentes
✅ Fácil agregar nuevas fuentes
✅ Cliente no conoce detalles de implementación
✅ Código limpio y mantenible

---

## 📝 ESTRUCTURA DE ARCHIVOS

```
patron-adapter-empleados/
│
├── src/
│   ├── database/                    [Consulta directa a BD]
│   │   ├── DatabaseConnection.java  → Conexión PostgreSQL
│   │   └── EmpleadoDB.java          → CLASE #1 (String → boolean)
│   │
│   ├── webservice/                  [Consulta vía Web Service]
│   │   ├── EmpleadoWS.java          → CLASE #2 (int → JSON)
│   │   └── EmpleadoWSResponse.java  → DTO de respuesta
│   │
│   ├── adapter/                     [Patrón Adapter]
│   │   ├── IEmpleadoAdapter.java    → Interfaz común (Target)
│   │   ├── EmpleadoUnificado.java   → Objeto unificado
│   │   ├── AdapterDB.java           → Adaptador #1
│   │   └── AdapterWS.java           → Adaptador #2
│   │
│   └── main/
│       └── Main.java                → Cliente (punto de entrada)
│
├── lib/                             [Librerías externas]
│   ├── postgresql-42.7.1.jar
│   └── gson-2.10.1.jar
│
├── bin/                             [Archivos compilados .class]
│
├── compilar.bat                     [Script de compilación]
├── ejecutar.bat                     [Script de ejecución]
└── README.md                        [Este archivo]
```

---

## 🚀 COMANDOS RÁPIDOS (RESUMEN)

```powershell
# 1. Crear estructura
mkdir patron-adapter-empleados
cd patron-adapter-empleados
mkdir src\database, src\webservice, src\adapter, src\main, lib, bin

# 2. Compilar
javac -encoding UTF-8 -cp "lib/*" -d bin src/database/*.java src/webservice/*.java src/adapter/*.java src/main/*.java

# 3. Ejecutar
java -cp "bin;lib/*" main.Main
```

---

## 📚 RECURSOS

- **Supabase**: https://supabase.com/docs
- **JDBC Tutorial**: https://docs.oracle.com/javase/tutorial/jdbc/
- **Patrón Adapter**: https://refactoring.guru/design-patterns/adapter
- **PostgreSQL JDBC**: https://jdbc.postgresql.org/documentation/

---

## ✅ CHECKLIST FINAL

Antes de entregar, verifica:

- [ ] JDK instalado y funcional
- [ ] Ambos .jar descargados en `lib/`
- [ ] Estructura de carpetas creada
- [ ] Supabase configurado con 10 empleados
- [ ] Credenciales en `DatabaseConnection.java`
- [ ] Código copiado en todos los archivos
- [ ] Proyecto compila sin errores
- [ ] Aplicación ejecuta correctamente
- [ ] Probadas las 3 opciones del menú
- [ ] Ambos métodos funcionan (DB y WS)

---

## 🎉 ¡LISTO!

Ahora tienes una aplicación completa que implementa el **Patrón Adapter** con:
- ✅ Dos fuentes de datos distintas
- ✅ Dos firmas de métodos diferentes
- ✅ PostgreSQL en Supabase
- ✅ Web Service REST simulado
- ✅ Interfaz unificada
- ✅ Código limpio y comentado

**¡Éxito con tu proyecto!** 🚀