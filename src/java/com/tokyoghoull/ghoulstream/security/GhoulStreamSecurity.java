package com.tokyoghoull.ghoulstream.security;

import android.content.Context;
import android.util.Log;

/**
 * 🛡️ GhoulStreamSecurity - Clase específica para GhoulStream Pro
 * 
 * Proporciona métodos predefinidos para proteger URLs y configuraciones
 * específicas de GhoulStream usando encriptación nativa.
 * 
 * @author TokyoghoulEs
 * @version 1.0.0
 * @since 2025-01-07
 */
public class GhoulStreamSecurity {
    
    private static final String TAG = "GhoulStreamSecurity";
    
    // 🔑 Clave maestra para GhoulStream (ofuscada dinámicamente)
    private static final String GHOULSTREAM_MASTER_KEY = buildMasterKey();
    
    // 📺 URLs encriptadas de contenido GhoulStream (ejemplos - reemplazar con URLs reales encriptadas)
    private static final String ENCRYPTED_CHANNELS_URL = "gH9sP3wM7qR5tV8yB2cF5jL9nQ2sU6xZ0aC4fH8kM1pT4vY7zB0eG3iL6oR9uX2w";
    private static final String ENCRYPTED_MOVIES_URL = "pQ4sT7vW0yZ3aC6fH9kL2nO5rU8xAbD1gI4lM7pS0vY3bE6hK9nQ2tW5zA8cF1i";
    private static final String ENCRYPTED_SERIES_URL = "tU7xA0bD3gH6jM9pS2vY5zA8cF1iL4oR7uX0eH3kN6qT9wV2yZ5aB8dG1fJ4mP7s";
    private static final String ENCRYPTED_API_ENDPOINT = "cF5jL9nQ2sU6xZ0aC4fH8kM1pT4vY7zB0eG3iL6oR9uX2wV5yZ8aB1dE4gH7kN0q";
    
    // 🌐 URLs de servicios GhoulStream
    private static final String ENCRYPTED_UPDATE_URL = "fH8kM1pT4vY7zB0eG3iL6oR9uX2wV5yZ8aB1dE4gH7kN0qT3vY6zA9cF2iL5oR8u";
    private static final String ENCRYPTED_EPG_URL = "iL6oR9uX2wV5yZ8aB1dE4gH7kN0qT3vY6zA9cF2iL5oR8uX1eH4kN7qT0wV3yZ6a";
    private static final String ENCRYPTED_XTREAM_CONFIG = "kN0qT3vY6zA9cF2iL5oR8uX1eH4kN7qT0wV3yZ6aB9dG2fJ5mP8sT1vW4xA7bD0g";
    
    // Estado de inicialización
    private static boolean isInitialized = false;
    private static Context appContext = null;
    
    /**
     * 🔑 Construye la clave maestra de forma dinámica y ofuscada
     * Múltiples capas de ofuscación para dificultar ingeniería inversa
     */
    private static String buildMasterKey() {
        // 🛡️ TÉCNICA 1: Construcción por partes
        String part1 = new StringBuilder("Ghoul").append("Stream").toString();
        String part2 = String.valueOf(2025);
        String part3 = new String(new char[]{'P', 'r', 'o', 'S', 'e', 'c', 'u', 'r', 'e'});
        String part4 = "Key";
        
        // 🛡️ TÉCNICA 2: Operaciones matemáticas
        int year = 2025;
        String yearStr = String.valueOf(year);
        
        // 🛡️ TÉCNICA 3: Manipulación de arrays
        char[] keyChars = (part1 + yearStr + part3 + part4).toCharArray();
        
        // 🛡️ TÉCNICA 4: StringBuilder con operaciones
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < keyChars.length; i++) {
            keyBuilder.append(keyChars[i]);
        }
        
        // 🛡️ TÉCNICA 5: Validación de integridad
        String finalKey = keyBuilder.toString();
        if (finalKey.length() != 27) {
            // Fallback ofuscado si algo falla
            return new String(new byte[]{71, 104, 111, 117, 108, 83, 116, 114, 101, 97, 109, 50, 48, 50, 53, 80, 114, 111, 83, 101, 99, 117, 114, 101, 75, 101, 121});
        }
        
        return finalKey;
    }
    
    /**
     * 🔐 Obtiene la clave maestra (solo para uso interno)
     * Método privado para acceso controlado
     */
    private static String getMasterKey() {
        return GHOULSTREAM_MASTER_KEY;
    }
    
    /**
     * 🚀 Inicializa el módulo de seguridad
     * @param context Contexto de la aplicación
     * @return true si la inicialización fue exitosa
     */
    public static boolean initialize(Context context) {
        if (isInitialized) {
            Log.d(TAG, "✅ Módulo ya inicializado");
            return true;
        }
        
        try {
            appContext = context.getApplicationContext();
            
            // Verificar disponibilidad de librería nativa
            if (!NativeEncryption.isAvailable()) {
                Log.e(TAG, "❌ Librería nativa no disponible");
                return false;
            }
            
            // Realizar self-test
            if (!NativeEncryption.performSelfTest()) {
                Log.e(TAG, "❌ Self-test de encriptación falló");
                return false;
            }
            
            isInitialized = true;
            Log.i(TAG, "✅ GhoulStream Security Module inicializado exitosamente");
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error inicializando módulo de seguridad: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 📺 Obtiene la URL por defecto de canales de TV
     * @return URL desencriptada de canales o null si hay error
     */
    public static String getDefaultChannelsUrl() {
        try {
            String url = NativeEncryption.decrypt(ENCRYPTED_CHANNELS_URL, getMasterKey());
            Log.d(TAG, "📺 URL de canales obtenida exitosamente");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo URL de canales: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🎬 Obtiene la URL por defecto de películas
     * @return URL desencriptada de películas o null si hay error
     */
    public static String getDefaultMoviesUrl() {
        try {
            String url = NativeEncryption.decrypt(ENCRYPTED_MOVIES_URL, getMasterKey());
            Log.d(TAG, "🎬 URL de películas obtenida exitosamente");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo URL de películas: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 📺 Obtiene la URL por defecto de series
     * @return URL desencriptada de series o null si hay error
     */
    public static String getDefaultSeriesUrl() {
        try {
            String url = NativeEncryption.decrypt(ENCRYPTED_SERIES_URL, getMasterKey());
            Log.d(TAG, "📺 URL de series obtenida exitosamente");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo URL de series: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔑 Obtiene el endpoint de API
     * @return URL desencriptada de API o null si hay error
     */
    public static String getApiEndpoint() {
        try {
            String url = NativeEncryption.decrypt(ENCRYPTED_API_ENDPOINT, getMasterKey());
            Log.d(TAG, "🔑 Endpoint de API obtenido exitosamente");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo endpoint de API: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔄 Obtiene la URL de actualización
     * @return URL desencriptada de actualización o null si hay error
     */
    public static String getUpdateUrl() {
        try {
            String url = NativeEncryption.decrypt(ENCRYPTED_UPDATE_URL, getMasterKey());
            Log.d(TAG, "🔄 URL de actualización obtenida exitosamente");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo URL de actualización: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 📡 Obtiene la URL de EPG
     * @return URL desencriptada de EPG o null si hay error
     */
    public static String getEpgUrl() {
        try {
            String url = NativeEncryption.decrypt(ENCRYPTED_EPG_URL, getMasterKey());
            Log.d(TAG, "📡 URL de EPG obtenida exitosamente");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo URL de EPG: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🎯 Obtiene la configuración de Xtream Codes
     * @return Configuración desencriptada o null si hay error
     */
    public static String getXtreamConfig() {
        try {
            String config = NativeEncryption.decrypt(ENCRYPTED_XTREAM_CONFIG, getMasterKey());
            Log.d(TAG, "🎯 Configuración de Xtream obtenida exitosamente");
            return config;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error obteniendo configuración de Xtream: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔐 Encripta una URL personalizada para almacenamiento seguro
     * @param url URL a encriptar
     * @return URL encriptada o null si hay error
     */
    public static String encryptCustomUrl(String url) {
        if (url == null || url.isEmpty()) {
            Log.w(TAG, "⚠️ URL vacía proporcionada para encriptación");
            return null;
        }
        
        try {
            String encrypted = NativeEncryption.encrypt(url, getMasterKey());
            Log.d(TAG, "🔐 URL personalizada encriptada exitosamente");
            return encrypted;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error encriptando URL personalizada: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔓 Desencripta una URL personalizada
     * @param encryptedUrl URL encriptada
     * @return URL desencriptada o null si hay error
     */
    public static String decryptCustomUrl(String encryptedUrl) {
        if (encryptedUrl == null || encryptedUrl.isEmpty()) {
            Log.w(TAG, "⚠️ URL encriptada vacía proporcionada");
            return null;
        }
        
        try {
            String decrypted = NativeEncryption.decrypt(encryptedUrl, getMasterKey());
            Log.d(TAG, "🔓 URL personalizada desencriptada exitosamente");
            return decrypted;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error desencriptando URL personalizada: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔐 Encripta configuración de usuario completa
     * @param config Configuración en formato JSON
     * @return Configuración encriptada o null si hay error
     */
    public static String encryptUserConfiguration(String config) {
        if (config == null || config.isEmpty()) {
            Log.w(TAG, "⚠️ Configuración vacía proporcionada");
            return null;
        }
        
        try {
            String encrypted = NativeEncryption.encrypt(config, getMasterKey());
            Log.d(TAG, "🔐 Configuración de usuario encriptada exitosamente");
            return encrypted;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error encriptando configuración: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔓 Desencripta configuración de usuario
     * @param encryptedConfig Configuración encriptada
     * @return Configuración desencriptada o null si hay error
     */
    public static String decryptUserConfiguration(String encryptedConfig) {
        if (encryptedConfig == null || encryptedConfig.isEmpty()) {
            Log.w(TAG, "⚠️ Configuración encriptada vacía proporcionada");
            return null;
        }
        
        try {
            String decrypted = NativeEncryption.decrypt(encryptedConfig, getMasterKey());
            Log.d(TAG, "🔓 Configuración de usuario desencriptada exitosamente");
            return decrypted;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error desencriptando configuración: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔐 Encripta credenciales de Xtream Codes
     * @param credentials Credenciales en formato JSON
     * @return Credenciales encriptadas o null si hay error
     */
    public static String encryptXtreamCredentials(String credentials) {
        if (credentials == null || credentials.isEmpty()) {
            Log.w(TAG, "⚠️ Credenciales vacías proporcionadas");
            return null;
        }
        
        try {
            String encrypted = NativeEncryption.encrypt(credentials, getMasterKey());
            Log.d(TAG, "🔐 Credenciales Xtream encriptadas exitosamente");
            return encrypted;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error encriptando credenciales Xtream: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🔓 Desencripta credenciales de Xtream Codes
     * @param encryptedCredentials Credenciales encriptadas
     * @return Credenciales desencriptadas o null si hay error
     */
    public static String decryptXtreamCredentials(String encryptedCredentials) {
        if (encryptedCredentials == null || encryptedCredentials.isEmpty()) {
            Log.w(TAG, "⚠️ Credenciales encriptadas vacías proporcionadas");
            return null;
        }
        
        try {
            String decrypted = NativeEncryption.decrypt(encryptedCredentials, getMasterKey());
            Log.d(TAG, "🔓 Credenciales Xtream desencriptadas exitosamente");
            return decrypted;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error desencriptando credenciales Xtream: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🧪 Realiza un test de seguridad completo
     * @return SecurityTestResult con los resultados del test
     */
    public static SecurityTestResult performSecurityTest() {
        Log.i(TAG, "🧪 Iniciando test completo de GhoulStreamSecurity...");
        
        SecurityTestResult result = new SecurityTestResult();
        
        try {
            // Test 1: Verificar disponibilidad de librería nativa
            if (!NativeEncryption.isAvailable()) {
                result.addError("Librería nativa no disponible");
                return result;
            }
            result.addFeature("Librería nativa disponible");
            
            // Test 2: Self-test de NativeEncryption
            if (!NativeEncryption.performSelfTest()) {
                result.addError("Self-test de NativeEncryption falló");
                return result;
            }
            result.addFeature("Self-test de encriptación exitoso");
            
            // Test 3: Verificar URLs predefinidas
            if (getDefaultChannelsUrl() != null) result.addFeature("URL de canales OK");
            if (getDefaultMoviesUrl() != null) result.addFeature("URL de películas OK");
            if (getDefaultSeriesUrl() != null) result.addFeature("URL de series OK");
            if (getApiEndpoint() != null) result.addFeature("Endpoint de API OK");
            if (getUpdateUrl() != null) result.addFeature("URL de actualización OK");
            if (getEpgUrl() != null) result.addFeature("URL de EPG OK");
            if (getXtreamConfig() != null) result.addFeature("Configuración Xtream OK");
            
            // Test 4: Test de encriptación/desencriptación personalizada
            String testUrl = "https://test-ghoulstream.com/api/v1/test";
            String encrypted = encryptCustomUrl(testUrl);
            String decrypted = decryptCustomUrl(encrypted);
            
            if (testUrl.equals(decrypted)) {
                result.addFeature("Encriptación/desencriptación personalizada OK");
            } else {
                result.addError("Encriptación/desencriptación personalizada falló");
            }
            
            // Test 5: Test de configuración de usuario
            String testConfig = "{\"theme\":\"dark\",\"language\":\"es\"}";
            String encryptedConfig = encryptUserConfiguration(testConfig);
            String decryptedConfig = decryptUserConfiguration(encryptedConfig);
            
            if (testConfig.equals(decryptedConfig)) {
                result.addFeature("Encriptación de configuración OK");
            } else {
                result.addError("Encriptación de configuración falló");
            }
            
            result.setValid(result.getErrors().isEmpty());
            
            if (result.isValid()) {
                Log.i(TAG, "✅ Test completo exitoso: todas las funcionalidades operativas");
            } else {
                Log.w(TAG, "⚠️ Test completo con errores: " + result.getErrors());
            }
            
        } catch (Exception e) {
            result.addError("Test completo falló con excepción: " + e.getMessage());
            Log.e(TAG, "❌ Test completo fallido", e);
        }
        
        return result;
    }
    
    /**
     * 📊 Obtiene información de diagnóstico completa
     * @return String con información detallada del sistema
     */
    public static String getGhoulStreamDiagnostics() {
        StringBuilder diagnostics = new StringBuilder();
        
        diagnostics.append("🛡️ GhoulStream Security Module - Diagnósticos\n");
        diagnostics.append("=".repeat(50)).append("\n\n");
        
        // Información de la librería nativa
        diagnostics.append("📚 LIBRERÍA NATIVA:\n");
        diagnostics.append(NativeEncryption.getDiagnosticInfo()).append("\n");
        
        // Test de URLs predefinidas
        diagnostics.append("📺 URLS PREDEFINIDAS:\n");
        diagnostics.append("• Canales: ").append(getDefaultChannelsUrl() != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostics.append("• Películas: ").append(getDefaultMoviesUrl() != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostics.append("• Series: ").append(getDefaultSeriesUrl() != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostics.append("• API: ").append(getApiEndpoint() != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostics.append("• Actualización: ").append(getUpdateUrl() != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostics.append("• EPG: ").append(getEpgUrl() != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostics.append("• Xtream Config: ").append(getXtreamConfig() != null ? "✅ OK" : "❌ ERROR").append("\n\n");
        
        // Test completo
        diagnostics.append("🧪 TEST COMPLETO:\n");
        SecurityTestResult testResult = performSecurityTest();
        diagnostics.append("• Resultado: ").append(testResult.isValid() ? "✅ PASS" : "❌ FAIL").append("\n");
        diagnostics.append("• Features: ").append(testResult.getFeatures().size()).append("\n");
        diagnostics.append("• Errores: ").append(testResult.getErrors().size()).append("\n\n");
        
        // Información del sistema
        diagnostics.append("📱 INFORMACIÓN DEL SISTEMA:\n");
        diagnostics.append("• Clave maestra: ").append(getMasterKey().length()).append(" caracteres\n");
        diagnostics.append("• URLs encriptadas: 7 configuradas\n");
        diagnostics.append("• Inicializado: ").append(isInitialized ? "✅ Sí" : "❌ No").append("\n");
        diagnostics.append("• Timestamp: ").append(System.currentTimeMillis()).append("\n");
        
        return diagnostics.toString();
    }
    
    /**
     * 🔧 Utilidad para desarrolladores: genera strings encriptados
     * Usar solo durante desarrollo para generar las constantes encriptadas
     * 
     * @param plainText Texto a encriptar
     * @return String encriptado para usar como constante
     */
    public static String generateEncryptedConstant(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            Log.w(TAG, "⚠️ Texto vacío para generar constante");
            return null;
        }
        
        String encrypted = NativeEncryption.encrypt(plainText, getMasterKey());
        if (encrypted != null) {
            Log.i(TAG, "🔧 Constante generada para: " + plainText.substring(0, Math.min(20, plainText.length())) + "...");
            Log.i(TAG, "📋 Usar en código: \"" + encrypted + "\"");
        }
        
        return encrypted;
    }
    
    /**
     * Valida firmas usando API legacy (API < 28)
     * Método separado para aislar el uso de API deprecada
     */
    @SuppressWarnings("deprecation") // Necesario para compatibilidad con API < 28
    private static boolean validateSignaturesLegacy(android.content.pm.PackageManager pm, Context appContext) {
        try {
            android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(
                appContext.getPackageName(), 
                android.content.pm.PackageManager.GET_SIGNATURES
            );
            android.content.pm.Signature[] signatures = packageInfo.signatures;
            return signatures != null && signatures.length > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error validando firmas legacy: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 🛡️ Detecta acceso root en el dispositivo (para flavor ultraSecure)
     * @return true si se detecta root
     */
    public static boolean detectRootAccess() {
        // Implementación básica - expandir en ultraSecure
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 🛡️ Detecta debugging activo (para flavor ultraSecure)
     * @return true si se detecta debugging
     */
    public static boolean detectDebugging() {
        return android.os.Debug.isDebuggerConnected();
    }
    
    /**
     * 🛡️ Valida integridad de la aplicación (para flavor ultraSecure)
     * @return true si la integridad es válida
     */
    public static boolean validateAppIntegrity() {
        // Implementación básica - expandir en ultraSecure
        if (appContext == null) {
            return false;
        }
        
        try {
            // Verificar firma de la aplicación con compatibilidad API
            android.content.pm.PackageManager pm = appContext.getPackageManager();
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                // API 28+ - usar GET_SIGNING_CERTIFICATES
                android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(
                    appContext.getPackageName(), 
                    android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES
                );
                return packageInfo.signingInfo != null && 
                       packageInfo.signingInfo.getApkContentsSigners() != null &&
                       packageInfo.signingInfo.getApkContentsSigners().length > 0;
            } else {
                // API < 28 - usar GET_SIGNATURES (deprecado pero necesario para compatibilidad)
                // Solución moderna: Extraer a método separado con anotación específica
                return validateSignaturesLegacy(pm, appContext);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error validando integridad: " + e.getMessage());
            return false;
        }
    }
}
