/*
 * Patrón Estructural - Flyweight
 * Clase: ListaReproduccion (Mejorada)
 */
package implementacion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lista de Reproducción - Representa el estado extrínseco (único para cada lista)
 */
public class ListaReproduccion {
    private String nombreLista;
    private List<Cancion> canciones = new ArrayList<>();
    // Contador de usos para priorizar las listas en memoria
    private long usos = 0L;
    
    public ListaReproduccion(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }
    
    public long getUsos() {
        return usos;
    }

    public void incrementarUso() {
        usos++;
    }

    public void setUsos(long usos) {
        this.usos = usos;
    }

    /**
     * Serializa la lista a un arreglo de bytes (Java Serialization).
     * Esto facilita almacenarla en la base de datos como bytea.
     */
    public byte[] toBytes() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this.nombreLista);
            oos.writeLong(this.usos);
            // Guardamos canciones como su información básica (id, nombre, artista.nombre)
            oos.writeInt(this.canciones.size());
            for (Cancion c : this.canciones) {
                oos.writeObject(c.getId());
                oos.writeObject(c.getNombreCancion());
                Artista a = c.getArtista();
                if (a != null) {
                    oos.writeObject(a.getId());
                    oos.writeObject(a.getNombreArtista());
                    oos.writeObject(a.getGeneroMusical());
                    oos.writeObject(a.getPais());
                } else {
                    oos.writeObject(null);
                    oos.writeObject(null);
                    oos.writeObject(null);
                    oos.writeObject(null);
                }
            }
            oos.flush();
            return baos.toByteArray();
        }
    }

    /**
     * Reconstruye una ListaReproduccion a partir de bytes guardados con toBytes().
     */
    public static ListaReproduccion fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            String nombre = (String) ois.readObject();
            long usos = ois.readLong();
            ListaReproduccion lista = new ListaReproduccion(nombre);
            lista.setUsos(usos);
            int size = ois.readInt();
            for (int i = 0; i < size; i++) {
                Object idObj = ois.readObject();
                Long id = idObj != null ? (Long) idObj : null;
                String nombreCancion = (String) ois.readObject();
                Object artistaIdObj = ois.readObject();
                if (artistaIdObj != null) {
                    Long artistaId = (Long) artistaIdObj;
                    String nombreArtista = (String) ois.readObject();
                    String genero = (String) ois.readObject();
                    String pais = (String) ois.readObject();
                    Artista artista = new Artista(artistaId, nombreArtista, genero, pais);
                    Cancion c = new Cancion(id, nombreCancion, artista);
                    lista.getCanciones().add(c);
                } else {
                    // artista nulo: consumir los 3 valores nulos
                    ois.readObject(); ois.readObject(); ois.readObject();
                    Cancion c = new Cancion(id, nombreCancion, null);
                    lista.getCanciones().add(c);
                }
            }
            return lista;
        }
    }
    
    /**
     * Agrega una canción a la lista (versión simple - compatibilidad)
     */
    public void addCancion(String nombreCancion) {
        canciones.add(FabricaCanciones.CrearItem(nombreCancion));
    }
    
    /**
     * MEJORADO: Agrega una canción con información completa del artista
     */
    public void addCancionConArtista(String nombreCancion, String nombreArtista, 
                                     String genero, String pais) {
        canciones.add(FabricaCanciones.crearCancion(nombreCancion, nombreArtista, genero, pais));
    }
    
    /**
     * Imprime la lista de reproducción con formato mejorado
     */
    public void imprimirLista() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║  🎵 LISTA: " + nombreLista);
        System.out.println("╠════════════════════════════════════════════════════════╣");
        
        if (canciones.isEmpty()) {
            System.out.println("║  (Lista vacía)");
        } else {
            for (int i = 0; i < canciones.size(); i++) {
                Cancion cancion = canciones.get(i);
                System.out.printf("║  %2d. %-30s - %-20s%n", 
                    (i + 1), 
                    cancion.getNombreCancion(),
                    cancion.getArtista() != null ? cancion.getArtista().getNombreArtista() : "Desconocido"
                );
            }
        }
        
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Versión simplificada de impresión
     */
    public void ImprimirLista() {
        String out = "\nPlayList > " + nombreLista;
        for (Cancion cancion : canciones) {
            out += "\n\t" + cancion.toString();
        }
        System.out.println(out);
    }
}