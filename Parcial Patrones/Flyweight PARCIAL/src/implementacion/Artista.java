/*
 * Patrón Estructural - Flyweight
 * Clase: Artista (Estado Intrínseco)
 */
package implementacion;

/**
 * Clase Artista - Representa el estado intrínseco compartido
 * Un artista puede tener muchas canciones, por lo que es reutilizable
 */
public class Artista {
    private Long id;
    private String nombreArtista;
    private String generoMusical;
    private String pais;
    
    public Artista(Long id, String nombreArtista, String generoMusical, String pais) {
        this.id = id;
        this.nombreArtista = nombreArtista;
        this.generoMusical = generoMusical;
        this.pais = pais;
    }

    public Artista() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public String getGeneroMusical() {
        return generoMusical;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "Artista{" + 
               "id=" + id + 
               ", nombre='" + nombreArtista + '\'' + 
               ", genero='" + generoMusical + '\'' + 
               ", pais='" + pais + '\'' + 
               '}';
    }
}