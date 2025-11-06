# 🔒 GhoulStream Security Module

<div align="center">

![Security Module](https://via.placeholder.com/150x150/B81D24/FFFFFF?text=🔒)

**Módulo de Seguridad Nativo para GhoulStream Pro**

[![C++](https://img.shields.io/badge/C%2B%2B-Native-00599C?style=flat-square&logo=c%2B%2B)](https://isocpp.org/)
[![JNI](https://img.shields.io/badge/JNI-Interface-ED8B00?style=flat-square&logo=java)](https://docs.oracle.com/javase/8/docs/technotes/guides/jni/)
[![Android](https://img.shields.io/badge/Android-9%2B-3DDC84?style=flat-square&logo=android)](https://developer.android.com)
[![Private](https://img.shields.io/badge/Repository-Private-red?style=flat-square)](LICENSE)

*Encriptación nativa y protección avanzada para GhoulStream Pro*

</div>

## 🛡️ **Descripción**

Este módulo proporciona **encriptación nativa C++** y **protección avanzada** para GhoulStream Pro. Está diseñado como un **submódulo Git independiente** que se integra con el proyecto principal para ofrecer diferentes niveles de seguridad.

### **Características de Seguridad**
- **Encriptación nativa C++** con librerías .so para 4 arquitecturas
- **Protección de configuraciones** sensibles del usuario
- **URLs seguras** para contenido premium y endpoints
- **Múltiples niveles** de protección (básica y ultra)
- **Fallbacks robustos** cuando el módulo no está disponible

## 🏗️ **Arquitectura del Módulo**

### **Estructura del Repositorio**
```
GhoulStream-security-module/
├── src/
│   ├── java/com/tokyoghoull/security/
│   │   ├── GhoulStreamSecurity.java      # API principal adaptada
│   │   ├── NativeEncryption.java         # JNI wrapper
│   │   ├── SecurityTestResult.java       # Resultado de tests
│   │   └── SecurityConfiguration.java    # Configuraciones
│   └── ultra/com/tokyoghoull/security/
│       ├── UltraSecurityManager.java     # Funciones avanzadas
│       └── AdvancedEncryption.java       # Encriptación ultra
├── libs/                                 # Librerías nativas básicas
│   ├── arm64-v8a/libencryption.so
│   ├── armeabi-v7a/libencryption.so
│   ├── x86/libencryption.so
│   └── x86_64/libencryption.so
├── libs/ultra/                           # Librerías ultra seguras
│   ├── arm64-v8a/libultra_security.so
│   ├── armeabi-v7a/libultra_security.so
│   ├── x86/libultra_security.so
│   └── x86_64/libultra_security.so
├── build.gradle                          # Configuración del módulo
└── README.md                             # Este archivo
```

### **Niveles de Seguridad**

| Nivel | Descripción | Librerías | Funcionalidades |
|-------|-------------|-----------|-----------------|
| **Standard** | Sin protección | ❌ Ninguna | Fallbacks únicamente |
| **Secure** | Protección básica | ✅ `libencryption.so` | Encriptación configuraciones |
| **Ultra Secure** | Máxima protección | ✅ `libencryption.so` + `libultra_security.so` | Encriptación + URLs seguras |

## 🔧 **API Principal**

### **GhoulStreamSecurity.java**
```java
public class GhoulStreamSecurity {
    
    // Inicialización
    public static native boolean initialize(Context context);
    
    // URLs seguras específicas de GhoulStream
    public static native String getDefaultChannelsUrl();
    public static native String getDefaultEPGUrl();
    
    // Encriptación de configuraciones
    public static native String encryptUserConfiguration(String config);
    public static native String decryptUserConfiguration(String encryptedConfig);
    
    // Protección de URLs de listas
    public static native String encryptPlaylistUrl(String url);
    public static native String decryptPlaylistUrl(String encryptedUrl);
    
    // Testing y diagnósticos
    public static native SecurityTestResult performSecurityTest();
    
    // Métodos específicos para GhoulStream Pro
    public static String getSecureChannelsUrl();
    public static String getSecureEPGUrl();
    public static String protectXtreamCredentials(String username, String password, String url);
    public static String[] unprotectXtreamCredentials(String encryptedCredentials);
}
```

### **Métodos Adaptados para GhoulStream**

#### **URLs Seguras**
```java
// URLs específicas de GhoulStream (no AceStream)
private static final String DEFAULT_CHANNELS_URL = "https://ghoulstream-cdn.tokyoghoull.com/channels.m3u";
private static final String DEFAULT_EPG_URL = "https://ghoulstream-cdn.tokyoghoull.com/epg.xml.gz";
private static final String FALLBACK_SERVER = "https://backup.ghoulstream.tokyoghoull.com/";
```

#### **Protección de Credenciales Xtream**
```java
public static String protectXtreamCredentials(String username, String password, String url) {
    try {
        String credentials = username + ":" + password + ":" + url;
        return encryptUserConfiguration(credentials);
    } catch (Exception e) {
        return credentials; // Fallback sin encriptar
    }
}
```

## 🔗 **Integración con Proyecto Principal**

### **Como Submódulo Git**
```bash
# En el repositorio principal GhoulStream
git submodule add https://github.com/TokyoghoulEs/GhoulStream-security-module.git security-module
git add .gitmodules security-module
git commit -m "Add security module as submodule"
```

### **Configuración .gitmodules**
```ini
[submodule "security-module"]
    path = security-module
    url = https://github.com/TokyoghoulEs/GhoulStream-security-module.git
    branch = main
    update = merge
```

### **Integración en build.gradle**
```gradle
// app/build.gradle del proyecto principal
sourceSets {
    secure {
        java.srcDirs = [
            'src/secure/java',
            '../security-module/src/java'
        ]
        jniLibs.srcDirs = [
            'src/secure/jniLibs',
            '../security-module/libs'
        ]
    }
    
    ultraSecure {
        java.srcDirs = [
            'src/secure/java',
            'src/ultraSecure/java',
            '../security-module/src/java',
            '../security-module/src/ultra'
        ]
        jniLibs.srcDirs = [
            'src/secure/jniLibs',
            'src/ultraSecure/jniLibs',
            '../security-module/libs',
            '../security-module/libs/ultra'
        ]
    }
}
```

## 🔄 **Adaptación desde AceStream**

### **Cambios Principales**
| Componente | AceStream Original | GhoulStream Adaptado |
|------------|-------------------|---------------------|
| **Clase Principal** | `AceStreamSecurity.java` | `GhoulStreamSecurity.java` |
| **URLs por Defecto** | AceStream CDN | GhoulStream CDN |
| **Métodos** | `getDefaultAceStreamUrl()` | `getSecureChannelsUrl()` |
| **Configuraciones** | AceStream específicas | GhoulStream + Xtream |
| **Fallbacks** | AceStream servers | GhoulStream servers |

### **URLs Adaptadas**
```java
// ANTES (AceStream):
"https://acestream-cdn.com/channels.m3u"
"https://acestream-api.com/epg.xml"

// DESPUÉS (GhoulStream):
"https://ghoulstream-cdn.tokyoghoull.com/channels.m3u"
"https://ghoulstream-cdn.tokyoghoull.com/epg.xml.gz"
```

### **Funcionalidades Nuevas**
- **Protección Xtream Codes**: Encriptación específica para credenciales IPTV
- **URLs de EPG**: Soporte para múltiples fuentes EPG
- **Configuraciones GhoulStream**: Adaptado para funcionalidades específicas

## 🛠️ **Desarrollo y Testing**

### **Compilación del Módulo**
```bash
# Compilar módulo independiente
./gradlew :security-module:build

# Verificar librerías nativas
ls -la libs/*/lib*.so
ls -la libs/ultra/*/lib*.so
```

### **Testing de Seguridad**
```java
// Test básico de inicialización
SecurityTestResult result = GhoulStreamSecurity.performSecurityTest();
if (result.isValid()) {
    Log.i("Security", "Module initialized successfully");
} else {
    Log.w("Security", "Module test failed: " + result.getErrors());
}

// Test de encriptación
String original = "test configuration";
String encrypted = GhoulStreamSecurity.encryptUserConfiguration(original);
String decrypted = GhoulStreamSecurity.decryptUserConfiguration(encrypted);
assert original.equals(decrypted);
```

### **Verificación de Arquitecturas**
```bash
# Verificar que las librerías existen para todas las arquitecturas
for arch in arm64-v8a armeabi-v7a x86 x86_64; do
    echo "Checking $arch:"
    ls -la libs/$arch/
    ls -la libs/ultra/$arch/
done
```

## 🔒 **Seguridad y Acceso**

### **Repositorio Privado**
- **Acceso restringido** solo a desarrolladores autorizados
- **Token de acceso** requerido para CI/CD (SUBMODULE_TOKEN)
- **Versionado independiente** del proyecto principal

### **Protección de Código**
- **Librerías nativas** compiladas y ofuscadas
- **Código fuente C++** no incluido en este repositorio
- **APIs JNI** como única interfaz pública

### **Fallbacks de Seguridad**
```java
// El proyecto principal funciona sin el módulo
public class SecurityManagerWrapper {
    public boolean initialize() {
        if (!BuildConfig.SECURITY_ENABLED) {
            return true; // No error, just disabled
        }
        
        try {
            // Intentar cargar módulo de seguridad
            return GhoulStreamSecurity.initialize(context);
        } catch (Exception e) {
            // Fallback: continuar sin seguridad
            Log.w(TAG, "Security module not available, using fallbacks");
            return false;
        }
    }
}
```

## 📋 **Configuración CI/CD**

### **GitHub Actions Integration**
```yaml
# En el workflow del proyecto principal
- name: Checkout code with submodules (Private Repos)
  uses: actions/checkout@v4
  with:
    submodules: recursive
    token: ${{ secrets.GITHUB_TOKEN }}
    fetch-depth: 0
```

### **Secrets Requeridos**
- `GITHUB_TOKEN`: Token automático para acceso a repos privados
- `SUBMODULE_TOKEN`: Token específico si se requiere acceso adicional

## 🔄 **Versionado y Updates**

### **Actualización del Submódulo**
```bash
# En el proyecto principal
git submodule update --remote security-module
git add security-module
git commit -m "Update security module to latest version"
```

### **Versionado Independiente**
- **Módulo de seguridad**: Versionado independiente (v1.0.0, v1.1.0, etc.)
- **Proyecto principal**: Referencias específicas al commit del módulo
- **Compatibilidad**: Mantenida entre versiones del módulo

## 📞 **Soporte y Contacto**

### **Desarrollador**
- **TokyoGhoull** - Desarrollador principal
- **Email**: [security@tokyoghoull.com](mailto:security@tokyoghoull.com)

### **Reportar Issues de Seguridad**
- **Vulnerabilidades**: Contactar directamente por email
- **Bugs del módulo**: Issues en este repositorio (acceso restringido)
- **Problemas de integración**: Issues en repositorio principal

## 📄 **Licencia**

**Módulo Privado y Propietario** - © 2024 TokyoGhoull

Este módulo es parte del ecosistema privado de GhoulStream Pro. Todos los derechos reservados.

---

<div align="center">

**🔒 GhoulStream Security Module**

*Protección nativa avanzada para GhoulStream Pro*

**Desarrollado por TokyoGhoull**

</div>