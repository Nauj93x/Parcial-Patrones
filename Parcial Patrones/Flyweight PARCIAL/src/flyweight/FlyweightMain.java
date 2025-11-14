/*
 * Patrón Estructural - Flyweight
 * Clase Principal - COMPLETO CON RETO
 */
package flyweight;

import implementacion.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Clase Principal - Demostración del Patrón Flyweight con Artistas
 */
public class FlyweightMain {

    // Datos para la demostración
    private static final String[][] CATALOGO_CANCIONES = {
        // {Nombre Canción, Artista, Género, País}
        {"Despacito", "Luis Fonsi", "Reggaeton", "Puerto Rico"},
        {"Shape of You", "Ed Sheeran", "Pop", "Reino Unido"},
        {"Blinding Lights", "The Weeknd", "Synthpop", "Canadá"},
        {"Dance Monkey", "Tones and I", "Pop", "Australia"},
        {"Someone You Loved", "Lewis Capaldi", "Pop", "Reino Unido"},
        {"Señorita", "Shawn Mendes", "Pop", "Canadá"},
        {"Bad Guy", "Billie Eilish", "Electropop", "Estados Unidos"},
        {"Roses", "SAINt JHN", "Hip Hop", "Estados Unidos"},
        {"Memories", "Maroon 5", "Pop Rock", "Estados Unidos"},
        {"Before You Go", "Lewis Capaldi", "Pop", "Reino Unido"}
    };
    
    private static final List<ListaReproduccion> listas = new ArrayList<>();
        
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     PATRÓN FLYWEIGHT - APLICACIÓN MUSICAL               ║");
        System.out.println("║     CON RETO: Artistas Reutilizables                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
    // Inyectar las credenciales que indicaste directamente en propiedades del sistema
    // NOTA: Esto incrusta secretos en el runtime; no es seguro para producción ni para subir a un repo.
    System.setProperty("SUPABASE_DATABASE_URL", "postgresql://postgres.gjhggyvnwbehtimzfwdw:jphc240205JPHC240205@aws-0-us-west-2.pooler.supabase.com:6543/postgres");
    System.setProperty("SUPABASE_PROJECT_URL", "https://gjhggyvnwbehtimzfwdw.supabase.co");
    System.setProperty("SUPABASE_API_KEY", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdqaGdneXZud2JlaHRpbXpmd2R3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMwNjMxOTcsImV4cCI6MjA3ODYzOTE5N30.F7yHc1nWEsQSAGLb4WWipm7QOMFR4TBDGtH6MQQOsho");

    // Crear un único Scanner sobre System.in y usarlo durante toda la ejecución
    Scanner scanner = new Scanner(System.in);
    // Intentar conectar a la BD antes de mostrar el menú
    startupConnect(scanner);

    while (true) {
        System.out.println("Elija una opción:");
        System.out.println("1) Ejecutar escenario con Flyweight (genera listas y persiste las menos usadas)");
        System.out.println("2) Listar playlists desde la BD (JDBC)");
        System.out.println("3) Listar playlists desde Supabase (REST + API key)");
    System.out.println("5) Crear tabla 'playlists' en la BD (si falta)");
        System.out.println("6) Vaciar tabla 'playlists' (borrar todo)");
        System.out.println("4) Salir");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1":
                // Ejecutar con Flyweight y 1000 listas por defecto (esto forzará evicción y persistencia)
                ejecutarEscenario(true, true, 1000);
                break;
            case "2":
                implementacion.DBManager.printStoredPlaylistsDetails();
                break;
            case "5":
                System.out.println("Intentando crear la tabla 'playlists' en la BD...");
                boolean ok = implementacion.DBManager.createPlaylistsTable();
                if (ok) System.out.println("Tabla creada/verificada. Ahora prueba opción 3 (REST) o 2 (JDBC) para confirmar.");
                break;
            case "6":
                System.out.print("¿Está seguro que desea VAC IAR la tabla 'playlists'? Esta operación borra todos los registros. (s/n): ");
                String conf = scanner.nextLine().trim().toLowerCase();
                if (conf.equals("s") || conf.equals("si") || conf.equals("y") || conf.equals("yes")) {
                    boolean wiped = implementacion.DBManager.clearPlaylistsTable();
                    if (wiped) System.out.println("Tabla vaciada correctamente.");
                    else System.out.println("No se pudo vaciar la tabla.");
                } else {
                    System.out.println("Operación cancelada.");
                }
                break;
            case "3":
                String proj = System.getProperty("SUPABASE_PROJECT_URL");
                if (proj == null || proj.isEmpty()) proj = System.getenv("SUPABASE_PROJECT_URL");
                String key = System.getProperty("SUPABASE_API_KEY");
                if (key == null || key.isEmpty()) key = System.getenv("SUPABASE_API_KEY");
                implementacion.SupabaseClient.listPlaylistsViaRest(proj, key);
                break;
            case "4":
                System.out.println("Saliendo...");
                scanner.close();
                return;
            default:
                System.out.println("Opción no válida. Intente de nuevo.\n");
        }
        System.out.println("\n--- Operación finalizada. Volviendo al menú. ---\n");
    }
    }
    
    /**
     * Demostración básica del patrón con artistas reutilizables
     */
    private static void demostracionBasica() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMOSTRACIÓN 1: CREACIÓN DE LISTAS CON ARTISTAS REUTILIZABLES");
        System.out.println("=".repeat(60) + "\n");
        
        // Crear listas de reproducción
        ListaReproduccion playlistPop = new ListaReproduccion(" Top Pop Hits");
        ListaReproduccion playlistFavoritos = new ListaReproduccion(" Mis Favoritas");
        ListaReproduccion playlistGym = new ListaReproduccion(" Workout Mix");
        
        System.out.println("\n--- Agregando canciones a 'Top Pop Hits' ---");
        playlistPop.addCancionConArtista("Shape of You", "Ed Sheeran", "Pop", "Reino Unido");
        playlistPop.addCancionConArtista("Bad Guy", "Billie Eilish", "Electropop", "Estados Unidos");
        playlistPop.addCancionConArtista("Someone You Loved", "Lewis Capaldi", "Pop", "Reino Unido");
        
        System.out.println("\n--- Agregando canciones a 'Mis Favoritas' ---");
        playlistFavoritos.addCancionConArtista("Shape of You", "Ed Sheeran", "Pop", "Reino Unido"); // REUTILIZADA
        playlistFavoritos.addCancionConArtista("Blinding Lights", "The Weeknd", "Synthpop", "Canadá");
        playlistFavoritos.addCancionConArtista("Before You Go", "Lewis Capaldi", "Pop", "Reino Unido");
        
        System.out.println("\n--- Agregando canciones a 'Workout Mix' ---");
        playlistGym.addCancionConArtista("Blinding Lights", "The Weeknd", "Synthpop", "Canadá"); // REUTILIZADA
        playlistGym.addCancionConArtista("Bad Guy", "Billie Eilish", "Electropop", "Estados Unidos"); // REUTILIZADA
        playlistGym.addCancionConArtista("Dance Monkey", "Tones and I", "Pop", "Australia");
        
        // Mostrar listas
        playlistPop.imprimirLista();
        playlistFavoritos.imprimirLista();
        playlistGym.imprimirLista();
        
        // Mostrar estadísticas
        FabricaCanciones.mostrarEstadisticas();
        FabricaArtistas.mostrarEstadisticas();
        
        System.out.println("🔍 ANÁLISIS:");
        System.out.println("   - Se crearon 3 listas con 9 canciones en total");
        System.out.println("   - Gracias al patrón Flyweight:");
        System.out.println("     • Solo " + FabricaCanciones.getTotalCancionesUnicas() + " objetos Canción en memoria");
        System.out.println("     • Solo " + FabricaArtistas.getTotalArtistasUnicos() + " objetos Artista en memoria");
        System.out.println("   - Ahorro de memoria: ~" + calcularAhorroMemoria(9, 
            FabricaCanciones.getTotalCancionesUnicas()) + "%\n");
    }

    /**
     * Intenta conectar a la base de datos antes de permitir al usuario continuar.
     * - Primero intenta usar variables/archivo ya configurado (DBManager.initFromEnv())
     * - Si falla, permite al usuario ingresar manualmente la URL (hasta 3 intentos)
     * - Si se conecta, verifica/crea la tabla 'playlists' y muestra las entradas actuales
     */
    private static void startupConnect(Scanner scanner) {
        System.out.println("[STARTUP] Comprobando configuración de la base de datos (SUPABASE_DATABASE_URL)...");
        implementacion.DBManager.initFromEnv();
        if (implementacion.DBManager.isEnabled()) {
            System.out.println("[STARTUP] Conexión establecida automáticamente desde variables/archivo.");
            implementacion.DBManager.createPlaylistsTable();
            implementacion.DBManager.printStoredPlaylistsDetails();
            return;
        }

        System.out.println("[STARTUP] No se pudo conectar automáticamente. Puede ingresar la URL JDBC o la URL tipo postgres:// para intentar conectar.");
        System.out.println("Ejemplo JDBC: jdbc:postgresql://host:5432/dbname?user=xxx&password=yyy");
        System.out.println("Ejemplo Supabase: postgres://user:password@host:port/postgres");

        int attempts = 0;
        while (attempts < 3 && !implementacion.DBManager.isEnabled()) {
            System.out.print("Ingrese la SUPABASE_DATABASE_URL (o presione ENTER para omitir): ");
            String input = "";
            if (scanner.hasNextLine()) {
                input = scanner.nextLine().trim();
            } else {
                System.out.println();
                System.out.println("[STARTUP] Entrada cerrada durante el ingreso de URL. Se omitirá.");
                input = "";
            }
            if (input == null || input.isEmpty()) {
                System.out.println("[STARTUP] Usuario omitió la URL. Se continuará sin persistencia.");
                break;
            }

            System.out.println("[STARTUP] Intentando conectar con la URL proporcionada...");
            implementacion.DBManager.initWithUrl(input);
            if (implementacion.DBManager.isEnabled()) {
                System.out.println("[STARTUP] Conexión exitosa.");
                implementacion.DBManager.createPlaylistsTable();
                implementacion.DBManager.printStoredPlaylistsDetails();
                // Guardar la URL en propiedades para siguientes ejecuciones
                System.setProperty("SUPABASE_DATABASE_URL", input);
                try {
                    String proj = System.getProperty("SUPABASE_PROJECT_URL", "");
                    String key = System.getProperty("SUPABASE_API_KEY", "");
                    saveSupabaseConfig(input, proj, key);
                } catch (Exception ex) {
                    // No crítico: solo informar
                    System.out.println("[STARTUP] No se pudo guardar supabase.properties: " + ex.getMessage());
                }
                break;
            } else {
                System.out.println("[STARTUP] Falló la conexión con la URL proporcionada. Revise credenciales y red. Intentos restantes: " + (2 - attempts));
            }
            attempts++;
        }

        if (!implementacion.DBManager.isEnabled()) {
            System.out.println("[STARTUP] Persistencia en BD queda deshabilitada. Puede configurar variables/archivo supabase.properties y reiniciar la aplicación.");
        }
    }
    
    /**
     * Prueba de rendimiento con gran cantidad de listas
     */
    private static void pruebaRendimiento() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMOSTRACIÓN 2: PRUEBA DE RENDIMIENTO A GRAN ESCALA");
        System.out.println("=".repeat(60) + "\n");
        
        Runtime runtime = Runtime.getRuntime();
        System.out.println("💾 Memoria máxima disponible: " + (runtime.maxMemory() / 1_000_000) + " MB");
        
        // Habilitar Flyweight
        FabricaCanciones.HabilitarFlyweight = true;
        
    System.out.println("\n🔄 Creando 1000 listas de reproducción...");
        System.out.println("   Cada lista tendrá 10 canciones aleatorias del catálogo");
        System.out.println("   (Este proceso puede tardar unos segundos)\n");
        
        long tiempoInicio = System.currentTimeMillis();
        crearListasDinamicas(1000);
        long tiempoFin = System.currentTimeMillis();
        
        // Calcular memoria usada
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        
        System.out.println("\n✅ PROCESO COMPLETADO\n");
        System.out.println("📊 RESULTADOS:");
        System.out.println("   • Total de listas creadas: " + listas.size());
        System.out.println("   • Total de canciones agregadas: " + (listas.size() * 10));
        System.out.println("   • Canciones únicas en memoria: " + FabricaCanciones.getTotalCancionesUnicas());
        System.out.println("   • Artistas únicos en memoria: " + FabricaArtistas.getTotalArtistasUnicos());
        System.out.println("   • Memoria utilizada: " + (memoryUsed / 1_000_000) + " MB");
        System.out.println("   • Tiempo de ejecución: " + (tiempoFin - tiempoInicio) + " ms");
        
        // Ahora transferimos las listas a una cache con capacidad limitada y persistimos las menos usadas
        PlaylistCache cache = new PlaylistCache(150); // mantener 150 listas en memoria como ejemplo
        System.out.println("\n📥 Agregando listas a la cache (capacidad " + 150 + ") y persistiendo las menos usadas...");
        for (ListaReproduccion l : listas) {
            // Simular algunos usos aleatorios para priorizar
            int usosAleatorios = new Random().nextInt(20);
            for (int k = 0; k < usosAleatorios; k++) l.incrementarUso();
            cache.add(l);
        }

        // Mostrar cuántas quedaron en la cache y cuántas se persistieron
        System.out.println("\n🔎 Estado final de la cache:");
        System.out.println("   • Listas en cache (memoria): " + cache.getAll().size());
        if (DBManager.isEnabled()) {
            System.out.println("   • Listas persistidas en BD: " + DBManager.listStoredPlaylists().size());
        } else {
            System.out.println("   • BD deshabilitada: no se guardaron playlists en la nube");
        }

        // Mostrar ahorro
        int totalCanciones = listas.size() * 10;
        double ahorro = calcularAhorroMemoria(totalCanciones, FabricaCanciones.getTotalCancionesUnicas());
        System.out.println("\n🎯 BENEFICIO DEL PATRÓN FLYWEIGHT:");
        System.out.println("   • Ahorro de memoria: ~" + ahorro + "%");
        System.out.println("   • Objetos evitados: " + (totalCanciones - FabricaCanciones.getTotalCancionesUnicas()));
        
        // Mostrar una muestra de listas
        System.out.println("\n📋 MUESTRA DE LISTAS CREADAS (primeras 3):");
        for (int i = 0; i < Math.min(3, listas.size()); i++) {
            listas.get(i).imprimirLista();
        }
    }
    
    /**
     * Crea listas de reproducción dinámicas con canciones aleatorias
     */
    private static void crearListasDinamicas(int numeroListas) {
        Random random = new Random();
        
        for (int i = 0; i < numeroListas; i++) {
            ListaReproduccion playlist = new ListaReproduccion("Playlist #" + (i + 1));
            
            // Agregar 10 canciones aleatorias del catálogo
            for (int j = 0; j < 10; j++) {
                int indiceAleatorio = random.nextInt(CATALOGO_CANCIONES.length);
                String[] datosCancion = CATALOGO_CANCIONES[indiceAleatorio];
                
                playlist.addCancionConArtista(
                    datosCancion[0], // Nombre canción
                    datosCancion[1], // Artista
                    datosCancion[2], // Género
                    datosCancion[3]  // País
                );
            }
            
            listas.add(playlist);
            
            // Mostrar progreso cada 10%
            if ((i + 1) % (numeroListas / 10) == 0) {
                int progreso = ((i + 1) * 100) / numeroListas;
                System.out.println("   Progreso: " + progreso + "% - Listas creadas: " + (i + 1));
            }
        }
    }

    /**
     * Crea y devuelve una lista de reproducción dinámica (útil para ejecuciones parametrizadas)
     */
    private static List<ListaReproduccion> crearListasDinamicasReturn(int numeroListas) {
        List<ListaReproduccion> result = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numeroListas; i++) {
            ListaReproduccion playlist = new ListaReproduccion("Playlist #" + (i + 1));

            for (int j = 0; j < 10; j++) {
                int indiceAleatorio = random.nextInt(CATALOGO_CANCIONES.length);
                String[] datosCancion = CATALOGO_CANCIONES[indiceAleatorio];

                playlist.addCancionConArtista(
                    datosCancion[0], // Nombre canción
                    datosCancion[1], // Artista
                    datosCancion[2], // Género
                    datosCancion[3]  // País
                );
            }

            result.add(playlist);

            if ((i + 1) % (numeroListas / 10) == 0) {
                int progreso = ((i + 1) * 100) / numeroListas;
                System.out.println("   Progreso: " + progreso + "% - Listas creadas: " + (i + 1));
            }
        }

        return result;
    }

    /**
     * Ejecuta un escenario con parámetros para Flyweight y muestra estadísticas y 3 playlists.
     */
    private static void ejecutarEscenario(boolean songsFly, boolean artistsFly, int numeroListas) {
        System.out.println("\n============================================");
        System.out.println("Escenario - Songs Flyweight: " + (songsFly ? "ON" : "OFF") + ", Artists Flyweight: " + (artistsFly ? "ON" : "OFF"));
        System.out.println("============================================\n");

        // Configurar y limpiar pools
        FabricaCanciones.HabilitarFlyweight = songsFly;
        FabricaArtistas.HabilitarFlyweight = artistsFly;
        FabricaCanciones.clearPool();
        FabricaArtistas.clearPool();

        Runtime runtime = Runtime.getRuntime();
        long beforeUsed = runtime.totalMemory() - runtime.freeMemory();

        long tiempoInicio = System.currentTimeMillis();
        List<ListaReproduccion> listasLocal = crearListasDinamicasReturn(numeroListas);
        long tiempoFin = System.currentTimeMillis();

        long afterUsed = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("\nRESULTADOS:");
        System.out.println("   • Total de listas creadas: " + listasLocal.size());
        System.out.println("   • Total de canciones agregadas: " + (listasLocal.size() * 10));
        System.out.println("   • Canciones únicas en pool: " + FabricaCanciones.getTotalCancionesUnicas());
        System.out.println("   • Artistas únicos en pool: " + FabricaArtistas.getTotalArtistasUnicos());
        System.out.println("   • Memoria usada (MB): " + ((afterUsed - beforeUsed) / 1_000_000.0));
        System.out.println("   • Tiempo de ejecución: " + (tiempoFin - tiempoInicio) + " ms");

        // Ahora movemos las listas a la cache con capacidad limitada y persistimos las menos usadas
        // La capacidad de la cache la calculamos en función del número de listas (por ejemplo 10% en memoria)
        int cacheCapacity = Math.max(1, numeroListas / 10);
        PlaylistCache cache = new PlaylistCache(cacheCapacity);
        System.out.println("\n📥 Agregando listas a la cache (capacidad " + cacheCapacity + ") y persistiendo las menos usadas...");

        Random rnd = new Random();
        for (ListaReproduccion l : listasLocal) {
            // Simular algunos usos aleatorios para priorizar
            int usosAleatorios = rnd.nextInt(20);
            for (int k = 0; k < usosAleatorios; k++) l.incrementarUso();
            cache.add(l);
        }

        // Mostrar cuántas quedaron en la cache y cuántas se persistieron
        System.out.println("\n🔎 Estado final de la cache:");
        System.out.println("   • Listas en cache (memoria): " + cache.getAll().size());
        if (implementacion.DBManager.isEnabled()) {
            System.out.println("   • Listas persistidas en BD: " + implementacion.DBManager.listStoredPlaylists().size());
        } else {
            System.out.println("   • BD deshabilitada: no se guardaron playlists en la nube");
        }

        // Mostrar 3 playlists finales (o menos si no hay suficientes)
        System.out.println("\n📋 TRES PLAYLISTS (muestra):");
        for (int i = 0; i < Math.min(3, cache.getAll().size()); i++) {
            cache.getAll().get(i).imprimirLista();
        }

        // Liberar y sugerir GC
        listasLocal.clear();
        System.gc();
        System.out.println("\n(Se solicitaron GC y limpieza de referencias)\n");
    }
    
    /**
     * Calcula el porcentaje de ahorro de memoria
     */
    private static double calcularAhorroMemoria(int objetosTotales, int objetosUnicos) {
        if (objetosTotales == 0) return 0;
        return ((objetosTotales - objetosUnicos) * 100.0) / objetosTotales;
    }

    private static String mask(String s) {
        if (s == null) return null;
        if (s.length() <= 8) return "****";
        return s.substring(0, 4) + "..." + s.substring(s.length() - 4);
    }

    private static void saveSupabaseConfig(String dbUrl, String projUrl, String apiKey) throws java.io.IOException {
        java.util.Properties p = new java.util.Properties();
        // Si ya existen en System properties, mantenerlas si el usuario dejó vacío el campo
        String curDb = System.getProperty("SUPABASE_DATABASE_URL");
        String curProj = System.getProperty("SUPABASE_PROJECT_URL");
        String curKey = System.getProperty("SUPABASE_API_KEY");

        if (curDb != null && !curDb.isEmpty()) p.setProperty("SUPABASE_DATABASE_URL", curDb);
        if (curProj != null && !curProj.isEmpty()) p.setProperty("SUPABASE_PROJECT_URL", curProj);
        if (curKey != null && !curKey.isEmpty()) p.setProperty("SUPABASE_API_KEY", curKey);

        // Si el usuario proveyó valores nuevos, sobrescribir
        if (dbUrl != null && !dbUrl.isEmpty()) p.setProperty("SUPABASE_DATABASE_URL", dbUrl);
        if (projUrl != null && !projUrl.isEmpty()) p.setProperty("SUPABASE_PROJECT_URL", projUrl);
        if (apiKey != null && !apiKey.isEmpty()) p.setProperty("SUPABASE_API_KEY", apiKey);

        java.io.File out = new java.io.File("supabase.properties");
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(out)) {
            p.store(fos, "Supabase configuration (auto-generated by FlyweightMain)");
        }
    }
}