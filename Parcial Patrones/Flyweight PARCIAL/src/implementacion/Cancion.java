/*
 * Patrón Estructural - Flyweight
 * Clase: Cancion (Mejorada con Artista)
 */
package implementacion;

/**
 * Clase Canción - Representa el estado intrínseco (compartido)
 * Ahora incluye una referencia al Artista (también reutilizable)
 */
public class Cancion {
    private Long id;
    private String nombreCancion;
    private Artista artista; // RETO: Asociación con Artista
    // Tamaño configurable del arreglo que simula los datos de la canción.
    // Se hace configurable para pruebas y evitar OOM en escenarios de prueba.
    public static int SAMPLE_DATA_SIZE = 10000; // por defecto 10KB (ajustable)
    private byte[] datosCancion = new byte[SAMPLE_DATA_SIZE]; // Simula el archivo de audio
    
    public Cancion(Long id, String nombreCancion, Artista artista) {
        this.id = id;
        this.nombreCancion = nombreCancion;
        this.artista = artista;
    }

    public Cancion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    public void setNombreCancion(String nombreCancion) {
        this.nombreCancion = nombreCancion;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    @Override
    public String toString() {
        return "Canción{" + 
               "id=" + id + 
               ", titulo='" + nombreCancion + '\'' + 
               ", artista=" + (artista != null ? artista.getNombreArtista() : "Desconocido") + 
               '}';
    }
}