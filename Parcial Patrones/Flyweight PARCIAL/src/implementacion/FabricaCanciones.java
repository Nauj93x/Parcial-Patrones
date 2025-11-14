/*
 * Patrón Estructural - Flyweight
 * Clase: FabricaCanciones (Mejorada)
 */
package implementacion;

import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica de Canciones - Gestiona el pool de objetos Canción
 * Mejorada para trabajar con Artistas reutilizables
 */
public class FabricaCanciones {
    
    public static boolean HabilitarFlyweight = true;
    private static final Map<String, Cancion> POOL_CANCIONES = new HashMap<>();
    private static Long Secuencia = 0L;
    
    /**
     * Crea o retorna una canción existente del pool
     * MEJORADO: Ahora incluye la información del artista
     */
    public static Cancion crearCancion(String nombreCancion, String nombreArtista, 
                                       String genero, String pais) {
        // Clave única: nombre de la canción + artista
        String clave = (nombreCancion + "-" + nombreArtista).toLowerCase();
        
        if (HabilitarFlyweight && POOL_CANCIONES.containsKey(clave)) {
            System.out.println("♻️  Reutilizando canción: " + nombreCancion + " - " + nombreArtista);
            return POOL_CANCIONES.get(clave);
        }

        // Obtener o crear el artista (también puede usar Flyweight según FabricaArtistas)
        Artista artista = FabricaArtistas.obtenerArtista(nombreArtista, genero, pais);

        // Crear nueva canción
        Cancion nuevaCancion = new Cancion(++Secuencia, nombreCancion, artista);

        // Solo guardamos en el pool si el Flyweight está habilitado
        if (HabilitarFlyweight) {
            POOL_CANCIONES.put(clave, nuevaCancion);
            System.out.println("✨ Creando nueva canción y almacenando en pool: " + nombreCancion + " - " + nombreArtista);
        } else {
            System.out.println("✨ Creando nueva canción (flyweight deshabilitado): " + nombreCancion + " - " + nombreArtista);
        }

        return nuevaCancion;
    }
    
    /**
     * Versión simplificada (compatibilidad con código anterior)
     */
    public static Cancion CrearItem(String nombreCancion) {
        return crearCancion(nombreCancion, "Artista Desconocido", "General", "Internacional");
    }
    
    /**
     * Obtiene el número total de canciones únicas en el pool
     */
    public static int getTotalCancionesUnicas() {
        return POOL_CANCIONES.size();
    }
    
    /**
     * Muestra estadísticas del pool de canciones
     */
    public static void mostrarEstadisticas() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📊 ESTADÍSTICAS DEL POOL DE CANCIONES");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Total de canciones únicas: " + POOL_CANCIONES.size());
        System.out.println("Flyweight habilitado: " + (HabilitarFlyweight ? "SÍ" : "NO"));
        System.out.println("═══════════════════════════════════════════\n");
    }

    /**
     * Limpia el pool de canciones (útil para pruebas comparativas en la misma JVM)
     */
    public static void clearPool() {
        POOL_CANCIONES.clear();
        Secuencia = 0L;
    }
}