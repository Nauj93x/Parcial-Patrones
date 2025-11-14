/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: IEmpleadoAdapter (Interface)
 * Descripción: Define el contrato común para ambos adaptadores
 */
package adapter;

/**
 * Interface común para los adaptadores (Target en patrón Adapter)
 * 
 * PROPÓSITO:
 * Define la interfaz UNIFICADA que el cliente usará.
 * Ambos adaptadores (AdapterDB y AdapterWS) implementan esta interfaz,
 * permitiendo que el cliente use cualquiera de forma transparente.
 * 
 * PATRÓN ADAPTER:
 * - Target (Objetivo): IEmpleadoAdapter
 * - Adaptee #1: EmpleadoDB (método: buscarEmpleadoPorCodigo(String))
 * - Adaptee #2: EmpleadoWS (método: getEmployeeByCode(int))
 * - Adapter #1: AdapterDB
 * - Adapter #2: AdapterWS
 * - Client: Main
 * 
 * @author Implementación Patrón Adapter
 */
public interface IEmpleadoAdapter {
    
    /**
     * Método unificado para consultar empleado
     * 
     * Esta es la interfaz COMÚN que ambos adaptadores deben implementar.
     * Aunque EmpleadoDB y EmpleadoWS tienen firmas diferentes:
     * - EmpleadoDB.buscarEmpleadoPorCodigo(String) → retorna boolean
     * - EmpleadoWS.getEmployeeByCode(int) → retorna String JSON
     * 
     * Ambos adaptadores los convierten a esta firma unificada:
     * - consultarEmpleado(String) → retorna EmpleadoUnificado
     * 
     * @param codigoEmpleado Código del empleado (formato: "EMP001")
     * @return EmpleadoUnificado objeto con datos del empleado, o null si no existe
     */
    public EmpleadoUnificado consultarEmpleado(String codigoEmpleado);
    
    /*
     * NOTA SOBRE EL PATRÓN:
     * 
     * El patrón Adapter resuelve el problema de incompatibilidad entre:
     * 
     * 1. SISTEMA LEGACY (EmpleadoDB):
     *    - Parámetro: String
     *    - Retorno: boolean (modifica estado interno)
     *    - Nombres: nombreCompleto, salarioMensual, areaTrabajo
     * 
     * 2. SISTEMA EXTERNO (EmpleadoWS):
     *    - Parámetro: int
     *    - Retorno: String JSON
     *    - Nombres en inglés: fullName, monthlySalary, department
     * 
     * 3. INTERFAZ UNIFICADA (IEmpleadoAdapter):
     *    - Parámetro: String (estándar)
     *    - Retorno: EmpleadoUnificado (objeto común)
     *    - Permite trabajar con ambos sistemas de forma transparente
     * 
     * VENTAJAS:
     * ✓ El cliente (Main) no necesita conocer las diferencias
     * ✓ Se pueden agregar nuevas fuentes de datos fácilmente
     * ✓ Cumple con Open/Closed Principle
     * ✓ Separa la lógica de adaptación del cliente
     */
}