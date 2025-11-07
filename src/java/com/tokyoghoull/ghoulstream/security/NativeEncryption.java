package com.tokyoghoull.ghoulstream.security;

import android.util.Log;

/**
 * 🛡️ NativeEncryption - Wrapper Java para BaseEncryption C++
 * 
 * Proporciona una interfaz Java fácil de usar para el sistema de encriptación nativo.
 * Protege strings sensibles usando libencryption.so para evitar ingeniería inversa.
 * 
 * @author TokyoghoulEs
 * @version 1.0.0
 * @since 2024-12-28
 */
public class NativeEncryption {
    
    private static final String TAG = "NativeEncryption";
    private static final String LIBRARY_NAME = "encryption";
    
    // Estado de inicialización
    private static boolean isInitialized = false;
    private static boolean initializationFailed = false;
    
    static {
        try {
            System.loadLibrary(LIBRARY_NAME);
            isInitialized = true;
            Log.i(TAG, "✅ Librería nativa cargada exitosamente: lib" + LIBRARY_NAME + ".so");
        } catch (UnsatisfiedLinkError e) {
            initializationFailed = true;
            Log.e(TAG, "❌ Error cargando librería nativa: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🔐 Encripta un string usando la clave proporcionada
     * 
     * @param plainText Texto a encriptar
     * @param key Clave de encriptación (mínimo 10 caracteres)
     * @return String encriptado o null si hay error
     * 
     * @example
     * String encrypted = NativeEncryption.encrypt("https://secret-url.com", "MySecretKey123");
     */
    public static String encrypt(String plainText, String key) {
        if (!validateInputs(plainText, key)) {
            return null;
        }
        
        try {
            String result = nativeEncrypt(plainText, key);
            Log.d(TAG, "🔐 String encriptado exitosamente (longitud: " + 
                  (result != null ? result.length() : 0) + ")");
            return result;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error en encriptación: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 🔓 Desencripta un string usando la clave proporcionada
     * 
     * @param encryptedText Texto encriptado
     * @param key Clave de desencriptación (debe coincidir con la de encriptación)
     * @return String desencriptado o null si hay error
     * 
     * @example
     * String decrypted = NativeEncryption.decrypt("xK9mP2vL8nQ4...", "MySecretKey123");
     */
    public static String decrypt(String encryptedText, String key) {
        if (!validateInputs(encryptedText, key)) {
            return null;
        }
        
        try {
            String result = nativeDecrypt(encryptedText, key);
            Log.d(TAG, "🔓 String desencriptado exitosamente (longitud: " + 
                  (result != null ? result.length() : 0) + ")");
            return result;
        } catch (Exception e) {
            Log.e(TAG, "❌ Error en desencriptación: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * ✅ Verifica si la librería nativa está disponible y funcionando
     * 
     * @return true si la librería está cargada correctamente
     */
    public static boolean isAvailable() {
        return isInitialized && !initializationFailed;
    }
    
    /**
     * 🧪 Realiza un test básico de funcionalidad
     * 
     * @return true si el test pasa correctamente
     */
    public static boolean performSelfTest() {
        if (!isAvailable()) {
            Log.w(TAG, "⚠️ Self-test fallido: librería no disponible");
            return false;
        }
        
        try {
            String testText = "GhoulStream Security Test";
            String testKey = "TestKey123456";
            
            // Test de encriptación
            String encrypted = encrypt(testText, testKey);
            if (encrypted == null || encrypted.isEmpty()) {
                Log.e(TAG, "❌ Self-test fallido: encriptación falló");
                return false;
            }
            
            // Test de desencriptación
            String decrypted = decrypt(encrypted, testKey);
            if (!testText.equals(decrypted)) {
                Log.e(TAG, "❌ Self-test fallido: desencriptación no coincide");
                return false;
            }
            
            Log.i(TAG, "✅ Self-test exitoso: encriptación/desencriptación funcionando");
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Self-test fallido con excepción: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 📊 Obtiene información sobre el estado de la librería
     * 
     * @return String con información de diagnóstico
     */
    public static String getDiagnosticInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🛡️ GhoulStream Security Module\n");
        info.append("📚 Librería: ").append(LIBRARY_NAME).append("\n");
        info.append("✅ Inicializada: ").append(isInitialized).append("\n");
        info.append("❌ Error inicialización: ").append(initializationFailed).append("\n");
        info.append("🔧 Disponible: ").append(isAvailable()).append("\n");
        
        if (isAvailable()) {
            boolean selfTest = performSelfTest();
            info.append("🧪 Self-test: ").append(selfTest ? "PASS" : "FAIL").append("\n");
        }
        
        return info.toString();
    }
    
    /**
     * 🔍 Valida los parámetros de entrada
     */
    private static boolean validateInputs(String text, String key) {
        if (!isAvailable()) {
            Log.e(TAG, "❌ Librería nativa no disponible");
            return false;
        }
        
        if (text == null || text.isEmpty()) {
            Log.e(TAG, "❌ Texto no puede ser null o vacío");
            return false;
        }
        
        if (key == null || key.length() < 10) {
            Log.e(TAG, "❌ Clave debe tener al menos 10 caracteres (actual: " + 
                  (key != null ? key.length() : 0) + ")");
            return false;
        }
        
        return true;
    }
    
    // Métodos nativos (implementados en C++)
    private static native String nativeEncrypt(String text, String key);
    private static native String nativeDecrypt(String text, String key);
}
