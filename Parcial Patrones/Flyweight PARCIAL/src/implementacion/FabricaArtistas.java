/*
 * Patrón Estructural - Flyweight
 * Clase: FabricaArtistas
 */
package implementacion;

import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica de Artistas - Gestiona el pool de objetos Artista reutilizables (Flyweight)
 */
public class FabricaArtistas {
    public static boolean HabilitarFlyweight = true;

    private static final Map<String, Artista> POOL_ARTISTAS = new HashMap<>();
    private static Long secuenciaArtista = 0L;
    
    /**
     * Crea o retorna un artista existente del pool
     * @param nombreArtista Nombre del artista
     * @param genero Género musical
     * @param pais País de origen
     * @return Objeto Artista (reutilizado o nuevo)
     */
    public static Artista obtenerArtista(String nombreArtista, String genero, String pais) {
        if (nombreArtista == null || nombreArtista.trim().isEmpty()) {
            nombreArtista = "Artista Desconocido";
        }
        // Usamos el nombre como clave única
        String clave = nombreArtista.trim().toLowerCase();

        if (HabilitarFlyweight && POOL_ARTISTAS.containsKey(clave)) {
            System.out.println("♻️  Reutilizando artista: " + nombreArtista);
            return POOL_ARTISTAS.get(clave);
        }

        // Crear nuevo artista (si flyweight está activo, lo almacenamos en el pool)
        Artista nuevoArtista = new Artista(++secuenciaArtista, nombreArtista, genero, pais);
        if (HabilitarFlyweight) {
            POOL_ARTISTAS.put(clave, nuevoArtista);
            System.out.println("✨ Creando nuevo artista y almacenando en pool: " + nombreArtista);
        } else {
            System.out.println("✨ Creando nuevo artista (flyweight deshabilitado): " + nombreArtista);
        }

        return nuevoArtista;
    }
    
    /**
     * Obtiene el número total de artistas únicos en el pool
     */
    public static int getTotalArtistasUnicos() {
        return POOL_ARTISTAS.size();
    }
    
    /**
     * Muestra estadísticas del pool de artistas
     */
    public static void mostrarEstadisticas() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📊 ESTADÍSTICAS DEL POOL DE ARTISTAS");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Total de artistas únicos: " + POOL_ARTISTAS.size());
        System.out.println("Artistas en memoria:");
        POOL_ARTISTAS.values().forEach(artista -> 
            System.out.println("  - " + artista.getNombreArtista() + 
                               " (" + artista.getGeneroMusical() + ")")
        );
        System.out.println("═══════════════════════════════════════════\n");
    }

    /**
     * Limpia el pool de artistas (útil para pruebas comparativas en la misma JVM)
     */
    public static void clearPool() {
        POOL_ARTISTAS.clear();
        secuenciaArtista = 0L;
    }
}