/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: EmpleadoUnificado
 * Descripción: Clase común que representa un empleado de forma unificada
 */
package adapter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Clase unificada que representa un empleado
 * 
 * PROPÓSITO:
 * Esta es la estructura COMÚN que usarán ambos adaptadores.
 * Unifica los datos de:
 * - EmpleadoDB (base de datos directa)
 * - EmpleadoWS (web service)
 * 
 * Permite que el cliente trabaje con un solo tipo de objeto,
 * sin importar de dónde vengan los datos.
 * 
 * @author Implementación Patrón Adapter
 */
public class EmpleadoUnificado {
    
    // Atributos unificados (nombres en español, estándar de la empresa)
    private String codigo;
    private String nombre;
    private String cargo;
    private double salario;
    private String departamento;
    private String email;
    private String fechaIngreso;
    
    /**
     * Constructor vacío
     */
    public EmpleadoUnificado() {
    }
    
    /**
     * Constructor completo
     */
    public EmpleadoUnificado(String codigo, String nombre, String cargo, 
                            double salario, String departamento, 
                            String email, String fechaIngreso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
        this.departamento = departamento;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
    }
    
    // ========================================
    // GETTERS Y SETTERS
    // ========================================
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public double getSalario() {
        return salario;
    }
    
    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    /**
     * Obtiene el salario formateado con separadores de miles
     */
    public String getSalarioFormateado() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return formatter.format(salario);
    }
    
    /**
     * Obtiene el primer nombre del empleado
     */
    public String getPrimerNombre() {
        if (nombre != null && nombre.contains(" ")) {
            return nombre.split(" ")[0];
        }
        return nombre;
    }
    
    /**
     * Verifica si el empleado es de tecnología
     */
    public boolean esDeTecnologia() {
        return departamento != null && 
               (departamento.toLowerCase().contains("tecnolog") ||
                departamento.toLowerCase().contains("desarrollo") ||
                departamento.toLowerCase().contains("infraestructura"));
    }
    
    /**
     * Representación en String con formato visual atractivo
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                   INFORMACIÓN DEL EMPLEADO                     ║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ 🆔 Código:        %-43s ║\n", codigo != null ? codigo : "N/A"));
        sb.append(String.format("║ 👤 Nombre:        %-43s ║\n", truncate(nombre, 43)));
        sb.append(String.format("║ 💼 Cargo:         %-43s ║\n", truncate(cargo, 43)));
        sb.append(String.format("║ 💰 Salario:       %-43s ║\n", getSalarioFormateado()));
        sb.append(String.format("║ 🏢 Departamento:  %-43s ║\n", truncate(departamento, 43)));
        sb.append(String.format("║ 📧 Email:         %-43s ║\n", truncate(email, 43)));
        sb.append(String.format("║ 📅 Fecha Ingreso: %-43s ║\n", fechaIngreso != null ? fechaIngreso : "N/A"));
        sb.append("╚════════════════════════════════════════════════════════════════╝");
        
        return sb.toString();
    }
    
    /**
     * Trunca un string a una longitud máxima
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Representación compacta para logs
     */
    public String toCompactString() {
        return String.format("Empleado{codigo='%s', nombre='%s', cargo='%s'}", 
                           codigo, nombre, cargo);
    }
}