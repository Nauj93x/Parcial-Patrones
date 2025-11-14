/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural - > Bridge + Factory
 * Tipo de Clase: Main()
 */
package patronbridge;

import factory.EncriptacionFactory;
import implementacion.InterfaceMensajeEncriptacion;

public class PatronBridgeMain {
    
    public static void main(String[] args) {
        System.out.println("=== PATRÓN BRIDGE + FACTORY CON CONFIGURACIÓN ===\n");
        
        // Crear el Factory
        EncriptacionFactory factory = new EncriptacionFactory();
        
        // Mostrar configuraciones disponibles
        factory.mostrarConfiguraciones();
        
        // Mensaje a encriptar
        final String message = "<Curso><Nombre>Patrones de Diseño de Software</Nombre></Curso>";
        
        try {
            // Crear Bridge para AES usando el Factory
            System.out.println("--- Encriptación con AES ---");
            InterfaceMensajeEncriptacion formatoAES = factory.crearBridge("encriptacion.aes");
            String messageAES = formatoAES.EncryptarMensaje(message, "HG58YZ3CR9123456");
            System.out.println("Formato AES > " + messageAES + "\n");
            
            // Crear Bridge para DES usando el Factory
            System.out.println("--- Encriptación con DES ---");
            InterfaceMensajeEncriptacion formatoDES = factory.crearBridge("encriptacion.des");
            String messageDES = formatoDES.EncryptarMensaje(message, "XMzDdG4D03CKm2Ix");
            System.out.println("Formato DES > " + messageDES + "\n");
            
            // Crear Bridge sin encriptación usando el Factory
            System.out.println("--- Sin Encriptación ---");
            InterfaceMensajeEncriptacion sinFormato = factory.crearBridge("encriptacion.none");
            String messageNO = sinFormato.EncryptarMensaje(message, null);
            System.out.println("Sin Formato > " + messageNO + "\n");
            
            // Demostración
            System.out.println("=== DEMOSTRACIÓN DE FLEXIBILIDAD ===");
            System.out.println("El Factory permite cambiar el algoritmo sin modificar el código,");
            System.out.println("solo modificando el archivo de configuración.\n");
            
            // Mostrar tipos disponibles
            System.out.println("Tipos de encriptación configurados:");
            String[] tipos = factory.obtenerTiposDisponibles();
            for (String tipo : tipos) {
                System.out.println("  - " + tipo);
            }
            
        } catch (Exception e) {
            System.err.println("Error durante la ejecución:");
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIN DE LA EJECUCIÓN ===");
    }
}