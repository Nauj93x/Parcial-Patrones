/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural - > Bridge + Factory
 * Tipo de Clase: Java - Factory
 */
package factory;

import encriptacion.InterfaceEncriptar;
import implementacion.InterfaceMensajeEncriptacion;
import implementacion.PuenteMensajeEncriptacion;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EncriptacionFactory {
    
    private static final String CONFIG_FILE = "config/encriptacion.properties";
    private Properties properties;
    
    public EncriptacionFactory() {
        properties = new Properties();
        cargarConfiguracion();
    }
    
    /**
     * Carga el archivo de configuración
     */
    private void cargarConfiguracion() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            System.out.println("Configuración cargada exitosamente desde: " + CONFIG_FILE);
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de configuración: " + ex.getMessage());
            System.err.println("Usando valores por defecto.");
        }
    }
    
    /**
     * Crea una instancia del Bridge según el tipo especificado en la configuración
     */
    public InterfaceMensajeEncriptacion crearBridge(String tipoConfig) throws Exception {
        String nombreClase = properties.getProperty(tipoConfig);
        
        if (nombreClase == null || nombreClase.isEmpty()) {
            throw new IllegalArgumentException("No se encontró configuración para: " + tipoConfig);
        }
        
        System.out.println("Creando Bridge con clase: " + nombreClase);
        
        // Usar reflexión para crear la instancia de la clase especificada
        Class<?> clase = Class.forName(nombreClase);
        InterfaceEncriptar procesoEncriptacion = (InterfaceEncriptar) clase.getDeclaredConstructor().newInstance();
        
        // Crear y retornar el puente con la implementación especificada
        return new PuenteMensajeEncriptacion(procesoEncriptacion);
    }
    
    /**
     * Obtiene todas las claves de configuración disponibles
     */
    public String[] obtenerTiposDisponibles() {
        return properties.stringPropertyNames().toArray(new String[0]);
    }
    
    /**
     * Muestra todas las configuraciones disponibles
     */
    public void mostrarConfiguraciones() {
        System.out.println("\n=== Configuraciones Disponibles ===");
        properties.forEach((key, value) -> 
            System.out.println(key + " = " + value)
        );
        System.out.println("===================================\n");
    }
}