/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: Main
 * Descripción: Punto de entrada de la aplicación - Cliente del patrón Adapter
 */
package main;

import adapter.AdapterDB;
import adapter.AdapterWS;
import adapter.EmpleadoUnificado;
import adapter.IEmpleadoAdapter;
import database.DatabaseConnection;
import java.util.Scanner;

/**
 * Clase principal que demuestra el patrón Adapter
 * 
 * CLIENTE DEL PATRÓN ADAPTER:
 * Esta clase usa la interfaz común IEmpleadoAdapter para trabajar
 * con múltiples fuentes de datos de forma transparente:
 * 
 * 1. Base de Datos (EmpleadoDB) vía AdapterDB
 * 2. Web Service (EmpleadoWS) vía AdapterWS
 * 
 * El cliente NO conoce los detalles de implementación de cada fuente,
 * solo usa la interfaz unificada.
 * 
 * @author Implementación Patrón Adapter
 */
public class Main {
    
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        try {
            // Mostrar banner inicial
            imprimirBanner();
            
            // Verificar conexión a base de datos
            if (!verificarConexion()) {
                System.err.println("\n✗ No se pudo establecer conexión a la base de datos");
                System.err.println("  Verifica las credenciales en DatabaseConnection.java");
                return;
            }
            
            // Loop principal de la aplicación
            boolean continuar = true;
            
            while (continuar) {
                mostrarMenu();
                int opcion = leerOpcion();
                
                switch (opcion) {
                    case 1:
                        consultarPorBaseDeDatos();
                        break;
                    case 2:
                        consultarPorWebService();
                        break;
                    case 3:
                        compararAmbosMetodos();
                        break;
                    case 4:
                        mostrarExplicacionPatron();
                        break;
                    case 5:
                        listarEmpleadosDisponibles();
                        break;
                    case 6:
                        continuar = false;
                        imprimirDespedida();
                        break;
                    default:
                        System.out.println("\n⚠ Opción no válida. Intente nuevamente.");
                }
                
                if (continuar && opcion >= 1 && opcion <= 3) {
                    System.out.print("\n¿Desea realizar otra consulta? (S/N): ");
                    String respuesta = scanner.nextLine().trim().toUpperCase();
                    if (!respuesta.equals("S")) {
                        continuar = false;
                        imprimirDespedida();
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("\n✗ Error crítico en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            DatabaseConnection.closeConnection();
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    /**
     * Consulta usando el adaptador de Base de Datos
     * Demuestra el uso de AdapterDB
     */
    private static void consultarPorBaseDeDatos() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println(" OPCIÓN 1: CONSULTA DIRECTA A BASE DE DATOS");
        System.out.println("═".repeat(65));
        
        System.out.print("\n→ Ingrese el código del empleado (ej: EMP001): ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        long inicio = System.currentTimeMillis();
        
        // PATRÓN ADAPTER: Usar la interfaz común
        IEmpleadoAdapter adapter = new AdapterDB();
        EmpleadoUnificado empleado = adapter.consultarEmpleado(codigo);
        
        long tiempo = System.currentTimeMillis() - inicio;
        
        if (empleado != null) {
            System.out.println(empleado);
            System.out.println("\n⏱ Tiempo de consulta: " + tiempo + "ms");
            System.out.println("✓ Fuente: Base de Datos PostgreSQL (Consulta Directa)");
        } else {
            System.out.println("\n❌ No se encontró el empleado con código: " + codigo);
        }
    }
    
    /**
     * Consulta usando el adaptador de Web Service
     * Demuestra el uso de AdapterWS
     */
    private static void consultarPorWebService() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println(" OPCIÓN 2: CONSULTA VÍA WEB SERVICE REST");
        System.out.println("═".repeat(65));
        
        System.out.print("\n→ Ingrese el código del empleado (ej: EMP002): ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        long inicio = System.currentTimeMillis();
        
        // PATRÓN ADAPTER: Usar la misma interfaz común
        IEmpleadoAdapter adapter = new AdapterWS();
        EmpleadoUnificado empleado = adapter.consultarEmpleado(codigo);
        
        long tiempo = System.currentTimeMillis() - inicio;
        
        if (empleado != null) {
            System.out.println(empleado);
            System.out.println("\n⏱ Tiempo de consulta: " + tiempo + "ms (incluye latencia de red simulada)");
            System.out.println("✓ Fuente: Web Service REST (SOA)");
        } else {
            System.out.println("\n❌ No se encontró el empleado con código: " + codigo);
        }
    }
    
    /**
     * Compara ambos métodos de consulta con el mismo código
     * Demuestra la transparencia del patrón Adapter
     */
    private static void compararAmbosMetodos() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println(" OPCIÓN 3: COMPARACIÓN DE AMBOS MÉTODOS");
        System.out.println("═".repeat(65));
        
        System.out.print("\n→ Ingrese el código del empleado (ej: EMP003): ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        System.out.println("\n" + "─".repeat(65));
        System.out.println("COMPARANDO MÉTODOS PARA: " + codigo);
        System.out.println("─".repeat(65));
        
        // Método 1: Base de Datos
        System.out.println("\n📊 MÉTODO 1: Consulta Directa a Base de Datos");
        System.out.println("─".repeat(65));
        long tiempoDB = System.currentTimeMillis();
        IEmpleadoAdapter adapterDB = new AdapterDB();
        EmpleadoUnificado empleadoDB = adapterDB.consultarEmpleado(codigo);
        tiempoDB = System.currentTimeMillis() - tiempoDB;
        
        if (empleadoDB != null) {
            System.out.println(empleadoDB);
            System.out.println("\n⏱ Tiempo: " + tiempoDB + "ms");
        }
        
        // Método 2: Web Service
        System.out.println("\n\n🌐 MÉTODO 2: Consulta mediante Web Service REST");
        System.out.println("─".repeat(65));
        long tiempoWS = System.currentTimeMillis();
        IEmpleadoAdapter adapterWS = new AdapterWS();
        EmpleadoUnificado empleadoWS = adapterWS.consultarEmpleado(codigo);
        tiempoWS = System.currentTimeMillis() - tiempoWS;
        
        if (empleadoWS != null) {
            System.out.println(empleadoWS);
            System.out.println("\n⏱ Tiempo: " + tiempoWS + "ms");
        }
        
        // Análisis de resultados
        System.out.println("\n" + "═".repeat(65));
        System.out.println(" ANÁLISIS DE RESULTADOS");
        System.out.println("═".repeat(65));
        
        if (empleadoDB != null && empleadoWS != null) {
            System.out.println("✓ Ambos métodos retornaron datos consistentes");
            System.out.println("✓ Mismo empleado desde dos fuentes diferentes");
            System.out.println("\n📈 Comparación de tiempos:");
            System.out.println("   Base de Datos:  " + tiempoDB + "ms");
            System.out.println("   Web Service:    " + tiempoWS + "ms");
            System.out.println("   Diferencia:     " + Math.abs(tiempoWS - tiempoDB) + "ms");
            
            if (tiempoDB < tiempoWS) {
                System.out.println("   → Base de Datos fue más rápida");
            } else {
                System.out.println("   → Web Service fue más rápida");
            }
            
            System.out.println("\n💡 VENTAJA DEL PATRÓN ADAPTER:");
            System.out.println("   Ambas fuentes usan la MISMA interfaz (IEmpleadoAdapter)");
            System.out.println("   El cliente no necesita conocer los detalles de cada fuente");
            System.out.println("   Se pueden agregar nuevas fuentes sin modificar el cliente");
        } else if (empleadoDB == null && empleadoWS == null) {
            System.out.println("❌ Empleado no encontrado en ninguna fuente");
        } else {
            System.out.println("⚠ Inconsistencia: Solo una fuente retornó datos");
        }
    }
    
    /**
     * Muestra explicación del patrón Adapter implementado
     */
    private static void mostrarExplicacionPatron() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println(" EXPLICACIÓN DEL PATRÓN ADAPTER");
        System.out.println("═".repeat(65));
        
        System.out.println("\n🎯 PROBLEMA QUE RESUELVE:");
        System.out.println("   Tenemos DOS clases incompatibles para consultar empleados:");
        System.out.println();
        System.out.println("   1️⃣ EmpleadoDB (Base de Datos Directa)");
        System.out.println("      • Método: buscarEmpleadoPorCodigo(String codigo)");
        System.out.println("      • Retorna: boolean (modifica estado interno)");
        System.out.println("      • Atributos: nombreCompleto, salarioMensual, areaTrabajo");
        System.out.println();
        System.out.println("   2️⃣ EmpleadoWS (Web Service REST)");
        System.out.println("      • Método: getEmployeeByCode(int employeeCode)");
        System.out.println("      • Retorna: String (JSON)");
        System.out.println("      • Atributos: fullName, monthlySalary, department (inglés)");
        
        System.out.println("\n🔧 SOLUCIÓN CON ADAPTER:");
        System.out.println("   Creamos dos adaptadores que convierten ambas clases");
        System.out.println("   a una interfaz común (IEmpleadoAdapter):");
        System.out.println();
        System.out.println("   📦 IEmpleadoAdapter (Interfaz Común)");
        System.out.println("      • Método: consultarEmpleado(String codigoEmpleado)");
        System.out.println("      • Retorna: EmpleadoUnificado");
        System.out.println();
        System.out.println("   🔌 AdapterDB adapta EmpleadoDB → IEmpleadoAdapter");
        System.out.println("   🔌 AdapterWS adapta EmpleadoWS → IEmpleadoAdapter");
        
        System.out.println("\n✨ VENTAJAS:");
        System.out.println("   ✓ El cliente usa UNA SOLA interfaz para ambas fuentes");
        System.out.println("   ✓ Fácil agregar nuevas fuentes de datos");
        System.out.println("   ✓ Cumple con Open/Closed Principle");
        System.out.println("   ✓ Separa la lógica de adaptación del cliente");
        System.out.println("   ✓ Mantiene el código limpio y mantenible");
        
        System.out.println("\n📊 ESTRUCTURA:");
        System.out.println("   Cliente (Main)");
        System.out.println("       ↓");
        System.out.println("   IEmpleadoAdapter (Target)");
        System.out.println("       ↓");
        System.out.println("   ├→ AdapterDB → EmpleadoDB (Adaptee 1)");
        System.out.println("   └→ AdapterWS → EmpleadoWS (Adaptee 2)");
        
        System.out.println("\n" + "═".repeat(65));
        esperarEnter();
    }
    
    /**
     * Lista los empleados disponibles en la base de datos
     */
    private static void listarEmpleadosDisponibles() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println(" EMPLEADOS DISPONIBLES PARA CONSULTA");
        System.out.println("═".repeat(65));
        
        System.out.println("\n📋 Códigos disponibles en la base de datos:");
        System.out.println();
        System.out.println("   EMP001 - Juan Pérez (Desarrollador Senior)");
        System.out.println("   EMP002 - María González (Analista de Datos)");
        System.out.println("   EMP003 - Carlos Rodríguez (Arquitecto de Software)");
        System.out.println("   EMP004 - Ana Martínez (DevOps Engineer)");
        System.out.println("   EMP005 - Luis Hernández (Product Manager)");
        System.out.println();
        System.out.println("💡 Puede usar cualquiera de estos códigos para las consultas");
        
        System.out.println("\n" + "═".repeat(65));
        esperarEnter();
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    /**
     * Verifica la conexión a la base de datos
     */
    private static boolean verificarConexion() {
        System.out.println("\n🔌 Verificando conexión a la base de datos...");
        return DatabaseConnection.getConnection() != null;
    }
    
    /**
     * Muestra el menú principal
     */
    private static void mostrarMenu() {
        System.out.println("\n" + "╔".repeat(65));
        System.out.println("║     SISTEMA DE CONSULTA DE EMPLEADOS - PATRÓN ADAPTER        ║");
        System.out.println("╠" + "═".repeat(63) + "╣");
        System.out.println("║  1. 📊 Consultar por Base de Datos (Método Directo)          ║");
        System.out.println("║  2. 🌐 Consultar por Web Service (Método REST)               ║");
        System.out.println("║  3. ⚖️  Comparar ambos métodos                                 ║");
        System.out.println("║  4. 📖 Explicación del Patrón Adapter                        ║");
        System.out.println("║  5. 📋 Listar empleados disponibles                          ║");
        System.out.println("║  6. 🚪 Salir                                                  ║");
        System.out.println("╚" + "═".repeat(63) + "╝");
        System.out.print("\n→ Seleccione una opción: ");
    }
    
    /**
     * Lee una opción del menú
     */
    private static int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            return opcion;
        } catch (Exception e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }
    
    /**
     * Espera que el usuario presione Enter
     */
    private static void esperarEnter() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Imprime el banner de inicio
     */
    private static void imprimirBanner() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println("║" + " ".repeat(63) + "║");
        System.out.println("║     PATRÓN ESTRUCTURAL: ADAPTER                              ║");
        System.out.println("║     Sistema de Consulta de Empleados                         ║");
        System.out.println("║                                                              ║");
        System.out.println("║     🔌 Adaptando múltiples fuentes de datos                  ║");
        System.out.println("║     📊 Base de Datos PostgreSQL (Supabase)                   ║");
        System.out.println("║     🌐 Web Service REST (SOA)                                ║");
        System.out.println("║                                                              ║");
        System.out.println("║     Asignatura: Patrones de Diseño de Software               ║");
        System.out.println("║" + " ".repeat(63) + "║");
        System.out.println("═".repeat(65));
    }
    
    /**
     * Imprime mensaje de despedida
     */
    private static void imprimirDespedida() {
        System.out.println("\n" + "═".repeat(65));
        System.out.println("║                                                              ║");
        System.out.println("║     ¡Gracias por usar el Sistema de Consulta de Empleados!  ║");
        System.out.println("║                                                              ║");
        System.out.println("║     Patrón Adapter implementado exitosamente                 ║");
        System.out.println("║     Dos fuentes de datos, una sola interfaz                  ║");
        System.out.println("║                                                              ║");
        System.out.println("║     👋 ¡Hasta luego!                                          ║");
        System.out.println("║                                                              ║");
        System.out.println("═".repeat(65));
    }
}