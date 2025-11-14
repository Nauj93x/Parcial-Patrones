/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: DatabaseConnection
 * Descripción: Maneja la conexión a PostgreSQL en Supabase
 */
package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para manejar la conexión a PostgreSQL (Supabase)
 * @author Implementación Patrón Adapter
 */
public class DatabaseConnection {
    
    // ============================================
    // CONFIGURACIÓN - Se intentará cargar desde `supabase.configs` o variables de entorno
    // Si no se encuentra el archivo, use los valores por defecto (editar manualmente)
    // ============================================
    // Valores por defecto (reemplazar si no desea usar archivo de configuración)
    private static String URL = "jdbc:postgresql://db.TU_PROYECTO_ID.supabase.co:5432/postgres";
    private static String USER = "postgres.TU_PROYECTO_ID";
    private static String PASSWORD = "TU_PASSWORD_AQUI";

    // Nombre del archivo de configuración (en la raíz del proyecto)
    private static final String CONFIG_FILE = "supabase.configs";

    // Cargar configuración al inicializar la clase
    static {
        loadConfig();
    }

    /**
     * Intentar cargar configuración desde el archivo `supabase.configs`.
     * Formato esperado: KEY=VALUE (puede contener comentarios con #)
     * Se soporta SUPABASE_DATABASE_URL en formato:
     *   postgresql://user:password@host:port/database
     * y/o las variables SUPABASE_PROJECT_URL, SUPABASE_API_KEY, etc.
     */
    private static void loadConfig() {
        Properties props = new Properties();

        // Primero, intentar leer variables de entorno (prioridad)
        String envUrl = System.getenv("SUPABASE_DATABASE_URL");
        String envUser = System.getenv("SUPABASE_DB_USER");
        String envPass = System.getenv("SUPABASE_DB_PASSWORD");

        if (envUrl != null && !envUrl.isEmpty()) {
            applyDatabaseUrl(envUrl);
            if (envUser != null) USER = envUser;
            if (envPass != null) PASSWORD = envPass;
            System.out.println("→ DatabaseConnection: cargado desde variables de entorno");
            return;
        }

        // Si no hay variables de entorno, intentar leer archivo supabase.configs
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);

            String fileDbUrl = props.getProperty("SUPABASE_DATABASE_URL");
            if (fileDbUrl != null && !fileDbUrl.isBlank()) {
                // El archivo podría contener caracteres escapados (\\:). Limpiar.
                fileDbUrl = fileDbUrl.replace("\\", "").trim();
                applyDatabaseUrl(fileDbUrl);
            }

            // También permitir variables separadas
            String fileUser = props.getProperty("SUPABASE_DB_USER");
            String filePass = props.getProperty("SUPABASE_DB_PASSWORD");
            if (fileUser != null && !fileUser.isBlank()) USER = fileUser.trim();
            if (filePass != null && !filePass.isBlank()) PASSWORD = filePass.trim();

            System.out.println("→ DatabaseConnection: cargado desde " + CONFIG_FILE);
        } catch (IOException e) {
            // No existe archivo o no se pudo leer: usar valores por defecto
            System.out.println("→ DatabaseConnection: no se encontró " + CONFIG_FILE + ", usando valores por defecto (o variables de entorno).");
        }
    }

    private static void applyDatabaseUrl(String dbUrl) {
        try {
            // URI no reconoce esquema 'postgresql' para el parsing de userinfo en todas las JDK,
            // así que reemplazamos temporalmente por 'http' para poder usar URI.
            String temp = dbUrl.trim();
            if (temp.startsWith("postgresql://") || temp.startsWith("postgres://")) {
                temp = temp.replaceFirst("^postgresql", "http");
                temp = temp.replaceFirst("^postgres", "http");
            }

            URI uri = new URI(temp);
            String userInfo = uri.getUserInfo();
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath(); // incluye '/postgres'

            if (host != null && port != -1 && path != null) {
                URL = "jdbc:postgresql://" + host + ":" + port + path;
            }

            if (userInfo != null) {
                String[] parts = userInfo.split(":" , 2);
                if (parts.length >= 1) USER = parts[0];
                if (parts.length == 2) PASSWORD = parts[1];
            }
        } catch (URISyntaxException e) {
            System.err.println("✗ DatabaseConnection: error parseando SUPABASE_DATABASE_URL: " + e.getMessage());
        }
    }
    
    private static Connection connection = null;
    
    /**
     * Obtiene la conexión a la base de datos PostgreSQL
     * Implementa patrón Singleton para la conexión
     * @return Connection objeto de conexión activo
     */
    public static Connection getConnection() {
        try {
            // Verificar si la conexión existe y está activa
            if (connection == null || connection.isClosed()) {
                // Cargar el driver de PostgreSQL
                Class.forName("org.postgresql.Driver");
                
                // Establecer la conexión
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                
                System.out.println("✓ Conexión a PostgreSQL establecida exitosamente");
                System.out.println("  Base de datos: Supabase");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("╔════════════════════════════════════════════════════════╗");
            System.err.println("║  ✗ ERROR: Driver de PostgreSQL no encontrado          ║");
            System.err.println("║                                                        ║");
            System.err.println("║  Solución:                                             ║");
            System.err.println("║  1. Verifica que postgresql-42.7.1.jar esté en lib/   ║");
            System.err.println("║  2. Compila con: javac -cp \"lib/*\" ...                 ║");
            System.err.println("╚════════════════════════════════════════════════════════╝");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("╔════════════════════════════════════════════════════════╗");
            System.err.println("║  ✗ ERROR: No se pudo conectar a la base de datos      ║");
            System.err.println("║                                                        ║");
            System.err.println("║  Verifica:                                             ║");
            System.err.println("║  1. Credenciales de Supabase correctas                ║");
            System.err.println("║  2. URL, USER y PASSWORD en DatabaseConnection.java   ║");
            System.err.println("║  3. Proyecto de Supabase activo                       ║");
            System.err.println("║  4. Conexión a internet                               ║");
            System.err.println("╚════════════════════════════════════════════════════════╝");
            System.err.println("\nDetalles del error:");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión a base de datos cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si la conexión está activa
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}