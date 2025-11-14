/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural - > Bridge + Factory
 * Tipo de Clase: Java
 */
package encriptacion;

public class ProcesoSinEncriptar implements InterfaceEncriptar {

    @Override
    public String encryptar(String message, String password) throws Exception {
        return message;
    }
}