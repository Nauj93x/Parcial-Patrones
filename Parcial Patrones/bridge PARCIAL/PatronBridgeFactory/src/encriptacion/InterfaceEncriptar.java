/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural - > Bridge + Factory
 * Tipo de Clase: Interface
 */
package encriptacion;

public interface InterfaceEncriptar {
    public String encryptar(String message, String password) throws Exception;
}