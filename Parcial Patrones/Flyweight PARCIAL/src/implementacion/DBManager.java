package implementacion;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * DBManager - gestor robusto para conectar a Postgres (Supabase)
 *
 * Lee configuración de System properties (-D), env vars o archivo supabase.properties.
 */
public class DBManager {
    private static Connection conn = null;

    public static void initFromEnv() {
        String databaseUrl = System.getProperty("SUPABASE_DATABASE_URL");
        if (isNullOrEmpty(databaseUrl)) databaseUrl = System.getenv("SUPABASE_DATABASE_URL");

        if (isNullOrEmpty(databaseUrl)) {
            java.io.File f = new java.io.File("supabase.properties");
            if (f.exists() && f.isFile()) {
                Properties p = new Properties();
                try (FileInputStream fis = new FileInputStream(f)) {
                    p.load(fis);
                    String fromFile = p.getProperty("SUPABASE_DATABASE_URL");
                    if (!isNullOrEmpty(fromFile)) {
                        databaseUrl = fromFile;
                        System.out.println("[DBManager] Cargada SUPABASE_DATABASE_URL desde supabase.properties");
                    }
                } catch (IOException ex) {
                    System.out.println("[DBManager] Error leyendo supabase.properties: " + ex.getMessage());
                }
            }
        }

        if (isNullOrEmpty(databaseUrl)) {
            System.out.println("[DBManager] No se encontró DATABASE_URL/SUPABASE_DATABASE_URL. Persistencia deshabilitada.");
            return;
        }

        initWithUrl(databaseUrl);
    }

    /**
     * Inicializa la conexión aceptando:
     * - jdbc:postgresql://host:port/db?...
     * - postgres://user:pass@host:port/db
     */
    public static void initWithUrl(String databaseUrl) {
        if (isNullOrEmpty(databaseUrl)) return;

        String jdbc = null;
        try {
            // Si ya es JDBC, conéctese directamente
            if (databaseUrl.startsWith("jdbc:postgresql://")) {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(databaseUrl);
                ensureTableExists();
                System.out.println("[DBManager] Conectado (JDBC direct). URL: " + maskJdbc(databaseUrl));
                return;
            }

            // Normalizar scheme postgres -> postgresql para URI
            String normalized = databaseUrl;
            if (databaseUrl.startsWith("postgres://")) {
                normalized = "postgresql://" + databaseUrl.substring("postgres://".length());
            }

            URI uri = new URI(normalized);
            String userInfo = uri.getUserInfo();
            String user = null;
            String pass = null;
            if (userInfo != null) {
                String[] up = userInfo.split(":", 2);
                user = up[0];
                pass = up.length > 1 ? up[1] : null;
            }

            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath(); // incluye leading '/'
            if (path == null || path.isEmpty()) path = "/postgres";

            jdbc = "jdbc:postgresql://" + host + (port != -1 ? (":" + port) : "") + path;

            Properties props = new Properties();
            if (user != null) props.setProperty("user", user);
            if (pass != null) props.setProperty("password", pass);

            // Forzar sslmode=require si no está presente (Supabase lo necesita)
            if (!jdbc.contains("sslmode")) jdbc += "?sslmode=require";

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(jdbc, props);
            ensureTableExists();
            System.out.println("[DBManager] Conectado (Supabase/Postgres). JDBC: " + maskJdbc(jdbc));

        } catch (URISyntaxException use) {
            System.out.println("[DBManager] URI inválida: " + use.getMessage());
            use.printStackTrace(System.out);
            conn = null;
        } catch (ClassNotFoundException cnf) {
            System.out.println("[DBManager] Driver PostgreSQL no encontrado en classpath: " + cnf.getMessage());
            cnf.printStackTrace(System.out);
            conn = null;
        } catch (SQLException sqle) {
            System.out.println("[DBManager] SQLException al conectar: " + sqle.getMessage());
            sqle.printStackTrace(System.out);
            conn = null;
        } catch (Exception e) {
            System.out.println("[DBManager] Error inicializando conexión: " + e.getMessage());
            if (jdbc != null) System.out.println("[DBManager] JDBC (masked) = " + maskJdbc(jdbc));
            e.printStackTrace(System.out);
            conn = null;
        }
    }

    private static String maskJdbc(String jdbc) {
        if (jdbc == null) return null;
        try {
            return jdbc.replaceAll("(//[^:]+:)([^@]+)(@)", "$1****$3");
        } catch (Exception ex) {
            return jdbc;
        }
    }

    private static void ensureTableExists() {
        if (conn == null) return;
        String ddl = "CREATE TABLE IF NOT EXISTS playlists (" +
                     "name TEXT PRIMARY KEY, data BYTEA NOT NULL, usos BIGINT DEFAULT 0, updated_at TIMESTAMP DEFAULT NOW()" +
                     ")";
        try (Statement st = conn.createStatement()) {
            st.execute(ddl);
        } catch (SQLException e) {
            System.out.println("[DBManager] Error creando/verificando tabla playlists: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean createPlaylistsTable() {
        if (!isEnabled()) {
            System.out.println("[DBManager] No hay conexión a la BD. No se puede crear la tabla.");
            return false;
        }
        try {
            ensureTableExists();
            System.out.println("[DBManager] Tabla 'playlists' creada o ya existente.");
            return true;
        } catch (Exception e) {
            System.out.println("[DBManager] Error creando la tabla: " + e.getMessage());
            return false;
        }
    }

    public static boolean isEnabled() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void savePlaylist(String name, byte[] data, long usos) {
        if (!isEnabled()) {
            System.out.println("[DBManager] Persistencia deshabilitada - no se guarda: " + name);
            return;
        }
        String sql = "INSERT INTO playlists(name, data, usos, updated_at) VALUES(?, ?, ?, now()) " +
                     "ON CONFLICT (name) DO UPDATE SET data = EXCLUDED.data, usos = EXCLUDED.usos, updated_at = now()";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setBytes(2, data);
            ps.setLong(3, usos);
            ps.executeUpdate();
            System.out.println("[DBManager] Playlist guardada: " + name + " (usos=" + usos + ")");
        } catch (SQLException e) {
            System.out.println("[DBManager] Error guardando playlist: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static ListaReproduccion loadPlaylist(String name) {
        if (!isEnabled()) {
            System.out.println("[DBManager] Persistencia deshabilitada - no se puede cargar: " + name);
            return null;
        }
        String sql = "SELECT data FROM playlists WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] data = rs.getBytes(1);
                    try {
                        return ListaReproduccion.fromBytes(data);
                    } catch (Exception ex) {
                        System.out.println("[DBManager] Error deserializando playlist: " + ex.getMessage());
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("[DBManager] Error cargando playlist: " + e.getMessage());
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static List<String> listStoredPlaylists() {
        List<String> result = new ArrayList<>();
        if (!isEnabled()) return result;
        String sql = "SELECT name FROM playlists ORDER BY updated_at DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("[DBManager] Error listando playlists: " + e.getMessage());
            e.printStackTrace(System.out);
        }
        return result;
    }

    public static void printStoredPlaylistsDetails() {
        if (!isEnabled()) {
            System.out.println("[DBManager] BD deshabilitada - no hay datos para mostrar.");
            return;
        }
        String sql = "SELECT name, usos, updated_at FROM playlists ORDER BY updated_at DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║  Playlists almacenadas en la base de datos   ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            int count = 0;
            while (rs.next()) {
                String name = rs.getString("name");
                long usos = rs.getLong("usos");
                Timestamp updated = rs.getTimestamp("updated_at");
                System.out.printf("║  %3d. %-30s  usos=%4d  updated=%s%n", ++count, name, usos, updated);
            }
            if (count == 0) System.out.println("║  (No hay playlists almacenadas)");
            System.out.println("╚══════════════════════════════════════════════╝\n");
        } catch (SQLException e) {
            System.out.println("[DBManager] Error obteniendo detalles de playlists: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
                System.out.println("[DBManager] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("[DBManager] Error cerrando conexión: " + e.getMessage());
        }
    }

    /**
     * Borra todo el contenido de la tabla playlists.
     * @return true si la operación fue exitosa
     */
    public static boolean clearPlaylistsTable() {
        if (!isEnabled()) {
            System.out.println("[DBManager] No hay conexión a la BD. No se puede vaciar la tabla.");
            return false;
        }
        String sql = "TRUNCATE TABLE playlists";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            System.out.println("[DBManager] Tabla 'playlists' vaciada correctamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("[DBManager] Error vaciando la tabla playlists: " + e.getMessage());
            e.printStackTrace(System.out);
            return false;
        }
    }
}