package com.tokyoghoull.ghoulstream.security;

import java.util.ArrayList;
import java.util.List;

/**
 * 🧪 SecurityTestResult - Resultado de tests de seguridad
 * 
 * Almacena los resultados de las pruebas de seguridad del módulo,
 * incluyendo features detectadas y errores encontrados.
 * 
 * @author TokyoghoulEs
 * @version 1.0.0
 * @since 2025-01-07
 */
public class SecurityTestResult {
    
    private boolean isValid;
    private List<String> features;
    private List<String> errors;
    private long timestamp;
    
    /**
     * Constructor por defecto
     */
    public SecurityTestResult() {
        this.isValid = false;
        this.features = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Agrega una feature detectada
     * @param feature Descripción de la feature
     */
    public void addFeature(String feature) {
        if (feature != null && !feature.isEmpty()) {
            features.add(feature);
        }
    }
    
    /**
     * Agrega un error encontrado
     * @param error Descripción del error
     */
    public void addError(String error) {
        if (error != null && !error.isEmpty()) {
            errors.add(error);
        }
    }
    
    /**
     * Establece si el test es válido
     * @param valid true si el test pasó exitosamente
     */
    public void setValid(boolean valid) {
        this.isValid = valid;
    }
    
    /**
     * Verifica si el test es válido
     * @return true si el test pasó exitosamente
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * Obtiene la lista de features detectadas
     * @return Lista de features
     */
    public List<String> getFeatures() {
        return new ArrayList<>(features);
    }
    
    /**
     * Obtiene la lista de errores encontrados
     * @return Lista de errores
     */
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    /**
     * Obtiene el timestamp del test
     * @return Timestamp en milisegundos
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Obtiene un resumen del resultado
     * @return String con el resumen
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("🧪 Test Result Summary\n");
        summary.append("Status: ").append(isValid ? "✅ PASS" : "❌ FAIL").append("\n");
        summary.append("Features: ").append(features.size()).append("\n");
        summary.append("Errors: ").append(errors.size()).append("\n");
        summary.append("Timestamp: ").append(timestamp).append("\n");
        
        if (!features.isEmpty()) {
            summary.append("\n✅ Features:\n");
            for (String feature : features) {
                summary.append("  • ").append(feature).append("\n");
            }
        }
        
        if (!errors.isEmpty()) {
            summary.append("\n❌ Errors:\n");
            for (String error : errors) {
                summary.append("  • ").append(error).append("\n");
            }
        }
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}
