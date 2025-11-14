Flyweight - Persistencia de Playlists en Supabase (Postgres)

Este proyecto muestra el patrón Flyweight y ha sido ampliado para persistir en PostgreSQL (por ejemplo Supabase) las listas de reproducción que menos se usan, manteniendo en memoria las más utilizadas mediante una cache.

Qué hace la extensión
- Inicializa una conexión a PostgreSQL si encuentra la variable de entorno SUPABASE_DATABASE_URL o DATABASE_URL.
- Mantiene una cache en memoria (PlaylistCache) con capacidad configurable. Al excederla, persiste la(s) listas menos usadas en la tabla playlists.
- La persistencia serializa la lista con Java Serialization y la guarda en una columna data BYTEA.

Requisitos
- Java 8+.
- Driver JDBC de PostgreSQL (postgresql-<version>.jar).

Configurar Supabase
1. Crear un proyecto en Supabase.
2. En la sección Settings -> Database, copiar la Connection string o DATABASE_URL (debe tener el formato postgres://user:pass@host:port/db).
3. Exportar la variable de entorno en Windows PowerShell antes de ejecutar. Por ejemplo:

    setx SUPABASE_DATABASE_URL "postgres://user:pass@host:port/db"

Además recomendamos definir estas variables para las llamadas REST a Supabase:

    setx SUPABASE_PROJECT_URL "https://<project>.supabase.co"
    setx SUPABASE_API_KEY "<tu_api_key_aqui>"

    (Cerrar y reabrir la terminal o reiniciar sesión para que la variable esté disponible.)

Añadir el driver JDBC
Descargar el jar del driver oficial de PostgreSQL (https://jdbc.postgresql.org/). Al compilar/ejecutar, añadirlo al classpath.

Ejemplo (PowerShell) para compilar y ejecutar desde este directorio (ajusta la ruta al jar):

    $jar = "C:\ruta\a\postgresql-42.5.0.jar"
    javac -cp $jar -d out src\implementacion\*.java src\flyweight\*.java
    java -cp "$jar;out" flyweight.FlyweightMain

Si no configura SUPABASE_DATABASE_URL, el programa seguirá funcionando en modo solo-memoria y no intentará persistir.

Notas de diseño
- La serialización usada es la de Java para simplicidad. En producción es recomendable usar JSON o un esquema claro.
- La tabla creada es playlists(name TEXT PRIMARY KEY, data BYTEA, usos BIGINT, updated_at TIMESTAMP).

Siguientes pasos (opcionales)
- Cambiar la serialización a JSON para inspección humana y compatibilidad.
- Implementar re-carga automática de playlists desde BD bajo demanda (DBManager.loadPlaylist).
- Añadir pruebas unitarias.
