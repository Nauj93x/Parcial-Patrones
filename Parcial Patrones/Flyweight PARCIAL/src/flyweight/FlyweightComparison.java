package flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import implementacion.FabricaArtistas;
import implementacion.FabricaCanciones;
import implementacion.ListaReproduccion;

/**
 * Prueba comparativa: ejecuta la creación de listas con Flyweight ON y OFF
 * en la misma JVM (limpiando pools entre pruebas) y muestra memoria y conteos.
 */
public class FlyweightComparison {

    private static final String[][] CATALOGO = {
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

    public static void main(String[] args) {
        System.out.println("Comparación Flyweight: ON vs OFF\n");

        // Escenario 1: Flyweight ON (canciones y artistas reutilizables)
        runScenario(true, true, 1000);

        // Escenario 2: Flyweight OFF (no se reutilizan canciones ni artistas)
        runScenario(false, false, 1000);
    }

    private static void runScenario(boolean songsEnabled, boolean artistsEnabled, int numeroListas) {
        System.out.println("============================================");
        System.out.println("Escenario - Songs Flyweight: " + (songsEnabled ? "ON" : "OFF")
                + ", Artists Flyweight: " + (artistsEnabled ? "ON" : "OFF"));
        System.out.println("============================================\n");

        // Configurar flags y limpiar pools
        FabricaCanciones.HabilitarFlyweight = songsEnabled;
        FabricaArtistas.HabilitarFlyweight = artistsEnabled;
        FabricaCanciones.clearPool();
        FabricaArtistas.clearPool();

        Runtime runtime = Runtime.getRuntime();

        long beforeUsed = runtime.totalMemory() - runtime.freeMemory();

        List<ListaReproduccion> listas = new ArrayList<>();
        Random random = new Random(12345);

        for (int i = 0; i < numeroListas; i++) {
            ListaReproduccion pl = new ListaReproduccion("Playlist #" + (i + 1));
            for (int j = 0; j < 10; j++) {
                int idx = random.nextInt(CATALOGO.length);
                String[] d = CATALOGO[idx];
                pl.addCancionConArtista(d[0], d[1], d[2], d[3]);
            }
            listas.add(pl);
            if ((i + 1) % (numeroListas / 10) == 0) {
                int progreso = ((i + 1) * 100) / numeroListas;
                System.out.println("Progreso: " + progreso + "% - Listas creadas: " + (i + 1));
            }
        }

        long afterUsed = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("\nRESULTADOS:");
        System.out.println("  Total listas: " + listas.size());
        System.out.println("  Total canciones agregadas: " + (listas.size() * 10));
        System.out.println("  Canciones únicas en pool: " + FabricaCanciones.getTotalCancionesUnicas());
        System.out.println("  Artistas únicos en pool: " + FabricaArtistas.getTotalArtistasUnicos());
        System.out.println("  Memoria usada (bytes): " + (afterUsed - beforeUsed));
        System.out.println("  Memoria usada (MB): " + ((afterUsed - beforeUsed) / 1_000_000.0));

        System.out.println("\n--- Muestra de 1 lista ---");
        listas.get(0).imprimirLista();

        // Liberar referencias para facilitar GC si se quiere
        listas.clear();
        System.gc();
        System.out.println("\n(Se limpió la lista y se solicitó GC)\n");
    }
}
