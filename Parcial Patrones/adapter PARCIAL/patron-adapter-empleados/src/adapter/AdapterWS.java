/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: AdapterWS
 * Descripción: Adaptador para EmpleadoWS (consulta via Web Service REST)
 */
package adapter;

import webservice.EmpleadoWS;
import webservice.EmpleadoWSResponse;

/**
 * Adaptador para la clase EmpleadoWS
 * 
 * PROPÓSITO:
 * Convierte la interfaz incompatible de EmpleadoWS a la interfaz
 * común IEmpleadoAdapter que el cliente espera usar.
 * 
 * ADAPTACIÓN:
 * - ORIGEN (Adaptee): EmpleadoWS.getEmployeeByCode(int) → String JSON
 * - DESTINO (Target): IEmpleadoAdapter.consultarEmpleado(String) → EmpleadoUnificado
 * 
 * RESPONSABILIDADES:
 * 1. Recibir parámetro String (interfaz unificada)
 * 2. Convertir String "EMP001" → int 1
 * 3. Llamar a EmpleadoWS.getEmployeeByCode(int)
 * 4. Parsear respuesta JSON
 * 5. Convertir a EmpleadoUnificado
 * 6. Retornar objeto unificado
 * 
 * DIFERENCIAS CLAVE vs AdapterDB:
 * - Conversión de tipos: String → int
 * - Parsing de JSON
 * - Manejo de respuesta asíncrona simulada
 * - Nombres de atributos en inglés
 * 
 * @author Implementación Patrón Adapter
 */
public class AdapterWS implements IEmpleadoAdapter {
    
    /**
     * Implementación del método unificado consultarEmpleado
     * 
     * Este método ADAPTA EmpleadoWS a la interfaz común:
     * - Recibe: String codigoEmpleado (interfaz unificada)
     * - Convierte: String "EMP001" → int 1 ← CONVERSIÓN DE TIPO
     * - Llama: EmpleadoWS.getEmployeeByCode(int) ← TIPO DIFERENTE
     * - Parsea: String JSON → EmpleadoWSResponse
     * - Convierte: EmpleadoWSResponse → EmpleadoUnificado
     * - Retorna: EmpleadoUnificado
     * 
     * @param codigoEmpleado Código del empleado (formato: "EMP001")
     * @return EmpleadoUnificado objeto unificado o null si no existe
     */
    @Override
    public EmpleadoUnificado consultarEmpleado(String codigoEmpleado) {
        System.out.println("\n" + "━".repeat(65));
        System.out.println("🔌 ADAPTER WS - Adaptando consulta a Web Service REST");
        System.out.println("━".repeat(65));
        System.out.println("🌐 Fuente de datos: Web Service REST (SOA)");
        System.out.println("🔧 Adaptando: EmpleadoWS → EmpleadoUnificado");
        System.out.println("   Método origen: getEmployeeByCode(int employeeCode)");
        System.out.println("   Método destino: consultarEmpleado(String codigoEmpleado)");
        System.out.println("━".repeat(65));
        
        // Validar parámetro
        if (codigoEmpleado == null || codigoEmpleado.trim().isEmpty()) {
            System.err.println("✗ Error: Código de empleado inválido");
            return null;
        }
        
        try {
            // PASO 1: Crear instancia del Web Service (Adaptee)
            EmpleadoWS empleadoWS = new EmpleadoWS();
            System.out.println("\n[1/5] Instancia de EmpleadoWS creada");
            
            // PASO 2: ADAPTACIÓN DE TIPO - Convertir String a int
            // Extraer número del código (EMP001 → 1, EMP002 → 2, etc.)
            int codigoNumerico;
            try {
                // Remover todo lo que no sea dígito y convertir a int
                codigoNumerico = Integer.parseInt(codigoEmpleado.replaceAll("\\D+", ""));
                System.out.println("[2/5] Conversión de tipo completada:");
                System.out.println("      String \"" + codigoEmpleado + "\" → int " + codigoNumerico);
            } catch (NumberFormatException e) {
                System.err.println("✗ Error: No se pudo extraer código numérico de: " + codigoEmpleado);
                System.err.println("  Formato esperado: EMP001, EMP002, etc.");
                return null;
            }
            
            // PASO 3: Llamar al Web Service con parámetro int
            // Firma original: String getEmployeeByCode(int employeeCode)
            System.out.println("[3/5] Invocando: empleadoWS.getEmployeeByCode(" + codigoNumerico + ")");
            System.out.println("      Esperando respuesta del servidor...");
            
            String jsonResponse = empleadoWS.getEmployeeByCode(codigoNumerico);
            
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.err.println("✗ Error: Respuesta vacía del Web Service");
                return null;
            }
            
            System.out.println("✓ [4/5] Respuesta recibida del Web Service");
            
            // PASO 4: Parsear JSON a objeto EmpleadoWSResponse
            EmpleadoWSResponse wsResponse = empleadoWS.parseResponse(jsonResponse);
            
            if (wsResponse == null) {
                System.err.println("✗ Error: No se pudo parsear la respuesta JSON");
                return null;
            }
            
            // Verificar si fue exitoso
            if (!wsResponse.isSuccess()) {
                System.out.println("✗ Web Service retornó error:");
                System.out.println("  Status Code: " + wsResponse.getStatusCode());
                System.out.println("  Mensaje: " + wsResponse.getMessage());
                System.out.println("⚠  Adaptador WS: Retornando null");
                return null;
            }
            
            System.out.println("✓ Respuesta exitosa del Web Service");
            System.out.println("  Status Code: " + wsResponse.getStatusCode());
            System.out.println("  Timestamp: " + wsResponse.getTimestamp());
            
            // PASO 5: ADAPTACIÓN - Convertir de EmpleadoWSResponse a EmpleadoUnificado
            System.out.println("[5/5] Convirtiendo EmpleadoWSResponse → EmpleadoUnificado");
            
            EmpleadoUnificado empleadoUnificado = new EmpleadoUnificado();
            
            // Mapeo de atributos (nombres en inglés → español)
            empleadoUnificado.setCodigo(wsResponse.getEmployeeId());
            empleadoUnificado.setNombre(wsResponse.getFullName());
            empleadoUnificado.setCargo(wsResponse.getJobTitle());
            empleadoUnificado.setSalario(wsResponse.getMonthlySalary());
            empleadoUnificado.setDepartamento(wsResponse.getDepartment());
            empleadoUnificado.setEmail(wsResponse.getEmailAddress());
            empleadoUnificado.setFechaIngreso(wsResponse.getHireDate());
            
            System.out.println("✓ Conversión completada exitosamente");
            System.out.println("\n📋 Mapeo de atributos (inglés → español):");
            System.out.println("   EmpleadoWSResponse.employeeId    → EmpleadoUnificado.codigo");
            System.out.println("   EmpleadoWSResponse.fullName      → EmpleadoUnificado.nombre");
            System.out.println("   EmpleadoWSResponse.jobTitle      → EmpleadoUnificado.cargo");
            System.out.println("   EmpleadoWSResponse.monthlySalary → EmpleadoUnificado.salario");
            System.out.println("   EmpleadoWSResponse.department    → EmpleadoUnificado.departamento");
            System.out.println("   EmpleadoWSResponse.emailAddress  → EmpleadoUnificado.email");
            System.out.println("   EmpleadoWSResponse.hireDate      → EmpleadoUnificado.fechaIngreso");
            
            System.out.println("\n✓ Adaptador WS: Adaptación completada");
            System.out.println("  Conversiones realizadas: String→int, JSON→Object, inglés→español");
            System.out.println("  Objeto tipo: " + empleadoUnificado.getClass().getSimpleName());
            System.out.println("  Empleado: " + empleadoUnificado.toCompactString());
            
            return empleadoUnificado;
            
        } catch (NumberFormatException e) {
            System.err.println("✗ Error de formato: El código '" + codigoEmpleado + "' no es válido");
            System.err.println("  Debe contener números (ejemplo: EMP001, EMP002)");
            return null;
        } catch (Exception e) {
            System.err.println("✗ Error en Adaptador WS: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Método toString para debug del adaptador
     */
    @Override
    public String toString() {
        return "AdapterWS{" +
                "adaptee=EmpleadoWS, " +
                "target=IEmpleadoAdapter, " +
                "source=REST Web Service, " +
                "conversions=String→int, JSON→Object" +
                "}";
    }
}