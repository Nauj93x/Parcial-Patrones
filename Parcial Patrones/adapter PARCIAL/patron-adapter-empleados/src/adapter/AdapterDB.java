/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: AdapterDB
 * Descripción: Adaptador para EmpleadoDB (consulta directa a base de datos)
 */
package adapter;

import database.EmpleadoDB;

/**
 * Adaptador para la clase EmpleadoDB
 * 
 * PROPÓSITO:
 * Convierte la interfaz incompatible de EmpleadoDB a la interfaz
 * común IEmpleadoAdapter que el cliente espera usar.
 * 
 * ADAPTACIÓN:
 * - ORIGEN (Adaptee): EmpleadoDB.buscarEmpleadoPorCodigo(String) → boolean
 * - DESTINO (Target): IEmpleadoAdapter.consultarEmpleado(String) → EmpleadoUnificado
 * 
 * RESPONSABILIDADES:
 * 1. Recibir parámetro String (interfaz unificada)
 * 2. Llamar a EmpleadoDB.buscarEmpleadoPorCodigo(String)
 * 3. Obtener datos usando getters de EmpleadoDB
 * 4. Convertir a EmpleadoUnificado
 * 5. Retornar objeto unificado
 * 
 * @author Implementación Patrón Adapter
 */
public class AdapterDB implements IEmpleadoAdapter {
    
    /**
     * Implementación del método unificado consultarEmpleado
     * 
     * Este método ADAPTA EmpleadoDB a la interfaz común:
     * - Recibe: String codigoEmpleado (igual que la interfaz)
     * - Llama: EmpleadoDB.buscarEmpleadoPorCodigo(String) ← MISMO TIPO
     * - Convierte: De EmpleadoDB a EmpleadoUnificado
     * - Retorna: EmpleadoUnificado
     * 
     * @param codigoEmpleado Código del empleado (formato: "EMP001")
     * @return EmpleadoUnificado objeto unificado o null si no existe
     */
    @Override
    public EmpleadoUnificado consultarEmpleado(String codigoEmpleado) {
        System.out.println("\n" + "━".repeat(65));
        System.out.println("🔌 ADAPTER DB - Adaptando consulta a Base de Datos Directa");
        System.out.println("━".repeat(65));
        System.out.println("📊 Fuente de datos: PostgreSQL (Supabase)");
        System.out.println("🔧 Adaptando: EmpleadoDB → EmpleadoUnificado");
        System.out.println("   Método origen: buscarEmpleadoPorCodigo(String codigo)");
        System.out.println("   Método destino: consultarEmpleado(String codigoEmpleado)");
        System.out.println("━".repeat(65));
        
        // Validar parámetro
        if (codigoEmpleado == null || codigoEmpleado.trim().isEmpty()) {
            System.err.println("✗ Error: Código de empleado inválido");
            return null;
        }
        
        try {
            // PASO 1: Crear instancia del sistema legacy (Adaptee)
            EmpleadoDB empleadoDB = new EmpleadoDB();
            System.out.println("\n[1/4] Instancia de EmpleadoDB creada");
            
            // PASO 2: Llamar al método con firma específica
            // Firma original: boolean buscarEmpleadoPorCodigo(String codigo)
            System.out.println("[2/4] Invocando: empleadoDB.buscarEmpleadoPorCodigo(\"" + codigoEmpleado + "\")");
            boolean encontrado = empleadoDB.buscarEmpleadoPorCodigo(codigoEmpleado);
            
            if (!encontrado) {
                System.out.println("✗ [3/4] Empleado no encontrado en base de datos");
                System.out.println("⚠  Adaptador DB: Retornando null");
                return null;
            }
            
            System.out.println("✓ [3/4] Empleado encontrado - Iniciando conversión");
            
            // PASO 3: ADAPTACIÓN - Convertir de EmpleadoDB a EmpleadoUnificado
            EmpleadoUnificado empleadoUnificado = new EmpleadoUnificado();
            
            // Mapeo de atributos (nombres diferentes en cada clase)
            empleadoUnificado.setCodigo(empleadoDB.getCodigoEmpleado());
            empleadoUnificado.setNombre(empleadoDB.getNombreCompleto());
            empleadoUnificado.setCargo(empleadoDB.getPuesto());
            empleadoUnificado.setSalario(empleadoDB.getSalarioMensual());
            empleadoUnificado.setDepartamento(empleadoDB.getAreaTrabajo());
            empleadoUnificado.setEmail(empleadoDB.getCorreoElectronico());
            empleadoUnificado.setFechaIngreso(empleadoDB.getFechaContratacion());
            
            System.out.println("✓ [4/4] Conversión completada exitosamente");
            System.out.println("\n📋 Mapeo de atributos:");
            System.out.println("   EmpleadoDB.codigoEmpleado    → EmpleadoUnificado.codigo");
            System.out.println("   EmpleadoDB.nombreCompleto    → EmpleadoUnificado.nombre");
            System.out.println("   EmpleadoDB.puesto            → EmpleadoUnificado.cargo");
            System.out.println("   EmpleadoDB.salarioMensual    → EmpleadoUnificado.salario");
            System.out.println("   EmpleadoDB.areaTrabajo       → EmpleadoUnificado.departamento");
            System.out.println("   EmpleadoDB.correoElectronico → EmpleadoUnificado.email");
            System.out.println("   EmpleadoDB.fechaContratacion → EmpleadoUnificado.fechaIngreso");
            
            System.out.println("\n✓ Adaptador DB: Adaptación completada");
            System.out.println("  Objeto tipo: " + empleadoUnificado.getClass().getSimpleName());
            System.out.println("  Empleado: " + empleadoUnificado.toCompactString());
            
            return empleadoUnificado;
            
        } catch (Exception e) {
            System.err.println("✗ Error en Adaptador DB: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Método toString para debug del adaptador
     */
    @Override
    public String toString() {
        return "AdapterDB{" +
                "adaptee=EmpleadoDB, " +
                "target=IEmpleadoAdapter, " +
                "source=PostgreSQL Direct" +
                "}";
    }
}