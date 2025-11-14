/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: EmpleadoWS
 * Descripción: Simula un Web Service REST para consultar empleados
 */
package webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Clase que simula un Web Service REST tipo SOA
 * FIRMA DEL MÉTODO: getEmployeeByCode(int employeeCode)
 * Esta es la firma DISTINTA #2 requerida en el parcial
 * @author Implementación Patrón Adapter
 */
public class EmpleadoWS {
    
    private Gson gson;
    
    /**
     * Constructor - Inicializa el serializador JSON
     */
    public EmpleadoWS() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    /**
     * Método para obtener empleado mediante Web Service REST
     * FIRMA ESPECÍFICA: Recibe int employeeCode y retorna JSON String
     * 
     * Diferencias con EmpleadoDB.buscarEmpleadoPorCodigo():
     * 1. Parámetro: int en vez de String
     * 2. Retorno: String JSON en vez de boolean
     * 3. Simula latencia de red (como un API real)
     * 
     * @param employeeCode Código numérico del empleado (ejemplo: 1 para EMP001)
     * @return String JSON con los datos del empleado o error
     */
    public String getEmployeeByCode(int employeeCode) {
        EmpleadoWSResponse response = new EmpleadoWSResponse();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            System.out.println("→ [WEB SERVICE] Procesando solicitud REST...");
            System.out.println("  Endpoint: GET /api/employees/" + employeeCode);
            
            // Simular latencia de red (como un API real en internet)
            Thread.sleep(800);
            System.out.println("  Latencia de red: 800ms");
            
            // Conectar a la base de datos
            conn = DatabaseConnection.getConnection();
            
            if (conn == null) {
                response.setSuccess(false);
                response.setMessage("Database connection error");
                response.setStatusCode(500);
                return gson.toJson(response);
            }
            
            // Convertir código numérico a formato EMP00X
            String codigoStr = String.format("EMP%03d", employeeCode);
            System.out.println("  Buscando empleado: " + codigoStr);
            
            // Query SQL
            String sql = "SELECT codigo, nombre, apellido, cargo, salario, " +
                        "departamento, email, fecha_ingreso " +
                        "FROM empleados " +
                        "WHERE codigo = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigoStr);
            
            rs = stmt.executeQuery();
            
            // Procesar resultado
            if (rs.next()) {
                // Mapear datos al objeto de respuesta
                response.setEmployeeId(rs.getString("codigo"));
                response.setFullName(rs.getString("nombre") + " " + rs.getString("apellido"));
                response.setJobTitle(rs.getString("cargo"));
                response.setMonthlySalary(rs.getDouble("salario"));
                response.setDepartment(rs.getString("departamento"));
                response.setEmailAddress(rs.getString("email"));
                response.setHireDate(rs.getDate("fecha_ingreso").toString());
                response.setSuccess(true);
                response.setMessage("Employee found successfully");
                response.setStatusCode(200);
                
                System.out.println("✓ [WS] Status: 200 OK");
                System.out.println("✓ [WS] Empleado encontrado: " + response.getFullName());
            } else {
                response.setSuccess(false);
                response.setMessage("Employee not found with code: " + codigoStr);
                response.setStatusCode(404);
                
                System.out.println("✗ [WS] Status: 404 Not Found");
            }
            
        } catch (InterruptedException e) {
            response.setSuccess(false);
            response.setMessage("Request interrupted");
            response.setStatusCode(500);
            System.err.println("✗ [WS] Request interrumpida");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(500);
            System.err.println("✗ [WS] Error interno del servidor");
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Convertir objeto a JSON y retornar
        String jsonResponse = gson.toJson(response);
        System.out.println("→ [WS] Respuesta JSON generada (" + jsonResponse.length() + " caracteres)");
        
        return jsonResponse;
    }
    
    /**
     * Método auxiliar para parsear la respuesta JSON
     * @param jsonResponse String JSON de respuesta
     * @return EmpleadoWSResponse objeto parseado
     */
    public EmpleadoWSResponse parseResponse(String jsonResponse) {
        try {
            return gson.fromJson(jsonResponse, EmpleadoWSResponse.class);
        } catch (Exception e) {
            System.err.println("✗ Error al parsear JSON: " + e.getMessage());
            return new EmpleadoWSResponse(false, "JSON parse error");
        }
    }
    
    /**
     * Método para obtener el JSON formateado (pretty print)
     * @param jsonResponse String JSON
     * @return String JSON formateado
     */
    public String getPrettyJson(String jsonResponse) {
        try {
            Object obj = gson.fromJson(jsonResponse, Object.class);
            return gson.toJson(obj);
        } catch (Exception e) {
            return jsonResponse;
        }
    }
}