/*
 * Asignatura: Patrones de Diseño de Software 
 * Patrón Estructural -> Adapter
 * Clase: EmpleadoWSResponse
 * Descripción: Objeto de respuesta del Web Service (formato JSON)
 */
package webservice;

/**
 * Clase que representa la respuesta del Web Service REST
 * Esta clase simula la respuesta de un API REST real
 * Los nombres de atributos están en inglés (convención REST/JSON)
 * @author Implementación Patrón Adapter
 */
public class EmpleadoWSResponse {
    
    // Atributos en inglés (estilo API REST)
    private String employeeId;
    private String fullName;
    private String jobTitle;
    private double monthlySalary;
    private String department;
    private String emailAddress;
    private String hireDate;
    
    // Metadatos de la respuesta
    private boolean success;
    private String message;
    private int statusCode;
    private long timestamp;
    
    /**
     * Constructor por defecto
     */
    public EmpleadoWSResponse() {
        this.success = false;
        this.statusCode = 500;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Constructor con parámetros básicos
     */
    public EmpleadoWSResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.statusCode = success ? 200 : 404;
        this.timestamp = System.currentTimeMillis();
    }
    
    // ========================================
    // GETTERS Y SETTERS
    // ========================================
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public double getMonthlySalary() {
        return monthlySalary;
    }
    
    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public String getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Método toString para debug
     */
    @Override
    public String toString() {
        return "EmpleadoWSResponse{" +
                "employeeId='" + employeeId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", success=" + success +
                ", statusCode=" + statusCode +
                '}';
    }
}