/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: EmpleadoDB
 * Descripción: Consulta empleados DIRECTAMENTE desde la base de datos
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que consulta empleados directamente desde PostgreSQL
 * FIRMA DEL MÉTODO: buscarEmpleadoPorCodigo(String codigo)
 * Esta clase tiene su propia estructura y firma de método
 * @author Implementación Patrón Adapter
 */
public class EmpleadoDB {
    
    // Atributos con nombres específicos de esta clase
    private String codigoEmpleado;
    private String nombreCompleto;
    private String puesto;
    private double salarioMensual;
    private String areaTrabajo;
    private String correoElectronico;
    private String fechaContratacion;
    
    /**
     * Método para buscar empleado por código en la base de datos
     * FIRMA ESPECÍFICA: Recibe String codigo
     * Esta es la firma DISTINTA #1 requerida en el parcial
     * 
     * @param codigo Código del empleado (ejemplo: "EMP001")
     * @return boolean true si encontró el empleado, false si no
     */
    public boolean buscarEmpleadoPorCodigo(String codigo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean encontrado = false;
        
        try {
            System.out.println("→ [BASE DE DATOS] Consultando empleado: " + codigo);
            
            // Obtener conexión
            conn = DatabaseConnection.getConnection();
            
            if (conn == null) {
                System.err.println("✗ [BD] Error: No hay conexión a la base de datos");
                return false;
            }
            
            // Query SQL con PreparedStatement (seguro contra SQL Injection)
            String sql = "SELECT codigo, nombre, apellido, cargo, salario, " +
                        "departamento, email, fecha_ingreso " +
                        "FROM empleados " +
                        "WHERE codigo = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            
            // Ejecutar consulta
            rs = stmt.executeQuery();
            
            // Procesar resultado
            if (rs.next()) {
                // Mapear columnas de BD a atributos de la clase
                this.codigoEmpleado = rs.getString("codigo");
                this.nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                this.puesto = rs.getString("cargo");
                this.salarioMensual = rs.getDouble("salario");
                this.areaTrabajo = rs.getString("departamento");
                this.correoElectronico = rs.getString("email");
                this.fechaContratacion = rs.getDate("fecha_ingreso").toString();
                
                encontrado = true;
                System.out.println("✓ [BD] Empleado encontrado en base de datos");
            } else {
                System.out.println("✗ [BD] Empleado con código '" + codigo + "' no encontrado");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ [BD] Error SQL al consultar la base de datos");
            System.err.println("  Query: SELECT ... FROM empleados WHERE codigo = '" + codigo + "'");
            e.printStackTrace();
        } finally {
            // Cerrar recursos (importante para evitar memory leaks)
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // NO cerramos la conexión aquí porque es singleton
            } catch (SQLException e) {
                System.err.println("✗ Error al cerrar recursos");
                e.printStackTrace();
            }
        }
        
        return encontrado;
    }
    
    // ========================================
    // GETTERS - Acceso a los datos consultados
    // ========================================
    
    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public String getPuesto() {
        return puesto;
    }
    
    public double getSalarioMensual() {
        return salarioMensual;
    }
    
    public String getAreaTrabajo() {
        return areaTrabajo;
    }
    
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    public String getFechaContratacion() {
        return fechaContratacion;
    }
    
    /**
     * Método toString para debug
     */
    @Override
    public String toString() {
        return "EmpleadoDB{" +
                "codigo='" + codigoEmpleado + '\'' +
                ", nombre='" + nombreCompleto + '\'' +
                ", puesto='" + puesto + '\'' +
                ", salario=" + salarioMensual +
                ", area='" + areaTrabajo + '\'' +
                '}';
    }
}