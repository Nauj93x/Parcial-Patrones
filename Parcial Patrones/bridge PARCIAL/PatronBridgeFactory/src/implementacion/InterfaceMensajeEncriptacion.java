/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural - > Bridge + Factory
 * Tipo de Clase: Interface
 */
package implementacion;

import encriptacion.InterfaceEncriptar;

public interface InterfaceMensajeEncriptacion {
    public String EncryptarMensaje(String message, String password) throws Exception;
}