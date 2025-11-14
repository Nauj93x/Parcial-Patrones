/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural - > Bridge + Factory
 * Tipo de Clase: Java
 */
package implementacion;

import encriptacion.InterfaceEncriptar;

public class PuenteMensajeEncriptacion implements InterfaceMensajeEncriptacion {
    private InterfaceEncriptar ProcesoEncryptacion;
    
    public PuenteMensajeEncriptacion(InterfaceEncriptar ProcesoEncryptacion) {
        this.ProcesoEncryptacion = ProcesoEncryptacion;
    }
    
    @Override
    public String EncryptarMensaje(String message, String password) throws Exception {
        return ProcesoEncryptacion.encryptar(message, password);
    }
}