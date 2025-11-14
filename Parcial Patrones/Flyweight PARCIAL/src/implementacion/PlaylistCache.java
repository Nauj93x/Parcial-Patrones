package implementacion;

import java.io.IOException;
import java.util.*;

/**
 * PlaylistCache - guarda en memoria las N listas más usadas.
 * Cuando se excede la capacidad, persiste la(s) menos usadas en la base de datos (si está habilitada).
 */
public class PlaylistCache {
    private final int maxEntries;
    private final Map<String, ListaReproduccion> cache = new HashMap<>();
    // Umbral por defecto: solo persistir playlists con usos < persistThreshold
    private final long persistThreshold;

    public PlaylistCache(int maxEntries) {
        this(maxEntries, 5);
    }

    /**
     * Construye la cache indicando capacidad máxima y umbral de persistencia.
     * @param maxEntries capacidad en memoria
     * @param persistThreshold solo persistir playlists con usos < persistThreshold
     */
    public PlaylistCache(int maxEntries, long persistThreshold) {
        this.maxEntries = Math.max(1, maxEntries);
        this.persistThreshold = Math.max(0, persistThreshold);
    }

    public synchronized void add(ListaReproduccion lista) {
        if (lista == null || lista.getNombreLista() == null) return;
        cache.put(lista.getNombreLista(), lista);
        ensureCapacity();
    }

    public synchronized ListaReproduccion get(String name) {
        return cache.get(name);
    }

    public synchronized void remove(String name) {
        cache.remove(name);
    }

    public synchronized List<ListaReproduccion> getAll() {
        return new ArrayList<>(cache.values());
    }

    private void ensureCapacity() {
        if (cache.size() <= maxEntries) return;

        // Buscar la lista con menor usos
        String minKey = null;
        long minUsos = Long.MAX_VALUE;
        for (Map.Entry<String, ListaReproduccion> e : cache.entrySet()) {
            long u = e.getValue().getUsos();
            if (u < minUsos) {
                minUsos = u;
                minKey = e.getKey();
            }
        }

        if (minKey != null) {
            ListaReproduccion evicted = cache.remove(minKey);
            // Decidir si persistir según umbral
            try {
                if (evicted.getUsos() < persistThreshold) {
                    if (DBManager.isEnabled()) {
                        byte[] data = evicted.toBytes();
                        DBManager.savePlaylist(evicted.getNombreLista(), data, evicted.getUsos());
                    } else {
                        System.out.println("[PlaylistCache] Evict (no persistido, BD deshabilitada): " + evicted.getNombreLista());
                    }
                } else {
                    // No persitir: dejamos que la playlist se elimine de la cache sin enviarla a la BD
                    System.out.println("[PlaylistCache] Evict sin persistir (usos=" + evicted.getUsos() + "): " + evicted.getNombreLista());
                }
            } catch (IOException ex) {
                System.out.println("[PlaylistCache] Error serializando playlist antes de persistir: " + ex.getMessage());
            }
        }
    }
}
