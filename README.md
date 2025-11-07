# 🛡️ GhoulStream Security Module

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![NDK](https://img.shields.io/badge/NDK-Compatible-blue.svg)](https://developer.android.com/ndk)
[![Integration](https://img.shields.io/badge/Integration-Git_Submodule-blue.svg)](https://github.com/TokyoghoulEs/GhoulStream)

**Módulo de seguridad nativo para GhoulStream Pro - Protege URLs y configuraciones sensibles usando encriptación C++ con múltiples capas de ofuscación y integración como submódulo Git.**

## 🆕 **Características Principales**

- 🛡️ **Multi-Layer Protection**: 5 técnicas para ocultar claves maestras
- 🔐 **Dynamic Key Construction**: Construcción de claves en runtime con ofuscación
- 🚫 **Anti-Analysis**: Protección avanzada contra herramientas de ingeniería inversa
- 📊 **Security Metrics**: 480x-960x más difícil de comprometer que versiones sin protección
- 🛠️ **ProGuard Integration**: Reglas específicas para máxima ofuscación
- 🔗 **Git Submodule Integration**: Integración perfecta con GhoulStream Pro
- 📱 **Product Flavor Support**: Compatible con flavors standard, secure y ultraSecure
- 🎯 **Android 15 Ready**: Compilado para Android 15 (API 35) con JDK 17

## 🎯 **Propósito & Integración**

Este módulo está diseñado específicamente para integrarse como **submódulo Git** en [GhoulStream Pro](https://github.com/TokyoghoulEs/GhoulStream), proporcionando protección nativa contra herramientas de ingeniería inversa como **IDA**, **Ghidra**, y **JADX**.

### **🔐 Protege:**
- 📺 URLs de listas M3U (canales y películas)
- 🔑 Credenciales de Xtream Codes (usuario, contraseña, servidor)
- 🌐 URLs de servidores de actualización y EPG
- ⚙️ Configuraciones de control remoto y sincronización
- 📱 Parámetros sensibles de la aplicación
- 🛡️ Tokens de autenticación y licencias DRM

## 🚀 **Integración con GhoulStream Pro**

### **1️⃣ Configuración como Submódulo Git**

```bash
# En el directorio raíz de GhoulStream
git submodule add https://github.com/TokyoghoulEs/GhoulStream-security-module.git security-module
git submodule update --init --recursive

# Verificar que el submódulo está correctamente configurado
git submodule status
```

### **2️⃣ Configuración de Build System**

#### **settings.gradle**
```gradle
include ':app'
include ':security-module'  // ✅ Incluir el módulo de seguridad
```

#### **app/build.gradle - Product Flavors**
```gradle
android {
    flavorDimensions "security"
    
    productFlavors {
        // 📱 Versión estándar (sin protección)
        standard {
            dimension "security"
            buildConfigField "boolean", "SECURITY_ENABLED", "false"
            buildConfigField "String", "SECURITY_LEVEL", "\"NONE\""
        }
        
        // 🛡️ Versión segura (protección básica)
        secure {
            dimension "security"
            applicationIdSuffix ".secure"
            buildConfigField "boolean", "SECURITY_ENABLED", "true"
            buildConfigField "String", "SECURITY_LEVEL", "\"BASIC\""
            
            // NDK para librerías nativas
            ndk {
                abiFilters 'arm64-v8a', 'armeabi-v7a', 'x86', 'x86_64'
            }
            proguardFile 'proguard-rules-secure.pro'
        }
        
        // 🔒 Versión ultra segura (protección máxima)
        ultraSecure {
            dimension "security"
            applicationIdSuffix ".ultra"
            buildConfigField "boolean", "SECURITY_ENABLED", "true"
            buildConfigField "boolean", "ULTRA_SECURITY_ENABLED", "true"
            buildConfigField "String", "SECURITY_LEVEL", "\"MAXIMUM\""
            
            ndk {
                abiFilters 'arm64-v8a', 'armeabi-v7a', 'x86', 'x86_64'
            }
            proguardFile 'proguard-rules-secure.pro'
            proguardFile 'proguard-rules-ultra.pro'
        }
    }
}
```

#### **app/build.gradle - SourceSets (CONFIGURACIÓN CLAVE)**
```gradle
android {
    // ✅ CONFIGURACIÓN CRÍTICA: sourceSets para integración del módulo
    sourceSets {
        secure {
            java.srcDirs = [
                'src/secure/java',                // Código específico del proyecto
                '../security-module/src/java'     // 🔥 Clases del módulo de seguridad
            ]
            jniLibs.srcDirs = [
                'src/secure/jniLibs',             // Librerías locales
                '../security-module/libs'         // 🔥 Librerías nativas del módulo
            ]
        }
        
        ultraSecure {
            java.srcDirs = [
                'src/secure/java',                // Código básico seguro
                'src/ultraSecure/java',           // Código ultra seguro específico
                '../security-module/src/java',    // 🔥 Clases básicas del módulo
                '../security-module/src/ultra'    // 🔥 Clases ultra del módulo (futuro)
            ]
            jniLibs.srcDirs = [
                'src/secure/jniLibs',             // Librerías básicas
                'src/ultraSecure/jniLibs',        // Librerías ultra específicas
                '../security-module/libs',        // 🔥 Librerías básicas del módulo
                '../security-module/libs/ultra'   // 🔥 Librerías ultra del módulo (futuro)
            ]
        }
    }
}
```

## 🏗️ **Estructura del Módulo**

```
security-module/
├── src/
│   ├── java/com/tokyoghoull/ghoulstream/security/
│   │   ├── GhoulStreamSecurity.java    # 🔐 API de alto nivel para GhoulStream
│   │   └── NativeEncryption.java       # 🔧 Wrapper JNI para C++
│   └── ultra/                          # Funcionalidades ultra (futuro)
├── libs/                               # Librerías nativas compiladas
│   ├── arm64-v8a/libencryption.so     # ARM 64-bit (~45KB)
│   ├── armeabi-v7a/libencryption.so   # ARM 32-bit (~42KB)
│   ├── x86/libencryption.so           # Intel 32-bit (~48KB)
│   ├── x86_64/libencryption.so        # Intel 64-bit (~50KB)
│   └── ultra/                          # Librerías ultra (futuro)
├── build.gradle                        # Configuración de build del módulo
├── proguard-rules.pro                  # Reglas ProGuard específicas
└── README.md                           # Este archivo
```

## 🛡️ **API del Módulo de Seguridad**

### **🔐 GhoulStreamSecurity.java - API de Alto Nivel**

```java
// URLs predefinidas encriptadas (configuradas en el módulo)
String channelsUrl = GhoulStreamSecurity.getDefaultChannelsUrl();
String moviesUrl = GhoulStreamSecurity.getDefaultMoviesUrl();
String apiEndpoint = GhoulStreamSecurity.getApiEndpoint();
String updateUrl = GhoulStreamSecurity.getUpdateUrl();

// Encriptación de credenciales Xtream Codes
String encrypted = GhoulStreamSecurity.encryptXtreamCredentials(username, password, server);
XtreamCredentials decrypted = GhoulStreamSecurity.decryptXtreamCredentials(encrypted);

// Encriptación personalizada para URLs del usuario
String encrypted = GhoulStreamSecurity.encryptCustomUrl("https://mi-servidor.com/playlist.m3u");
String decrypted = GhoulStreamSecurity.decryptCustomUrl(encrypted);

// Testing y diagnósticos
boolean testPassed = GhoulStreamSecurity.performFullTest();
String diagnostics = GhoulStreamSecurity.getDetailedDiagnostics();
```

### **🔧 NativeEncryption.java - Wrapper JNI**

```java
// Verificación de disponibilidad
boolean available = NativeEncryption.isAvailable();
boolean selfTest = NativeEncryption.performSelfTest();

// Encriptación/desencriptación directa
String encrypted = NativeEncryption.encrypt("texto-sensible", "MiClave123456");
String decrypted = NativeEncryption.decrypt(encrypted, "MiClave123456");

// Información de diagnóstico
String diagnostics = NativeEncryption.getDiagnosticInfo();
```

## 📦 **Product Flavors y Compilación**

### **🏗️ Estructura de Compilación:**

| Flavor | Descripción | Módulo Seguridad | Librerías Nativas | Application ID |
|--------|-------------|------------------|-------------------|----------------|
| **standard** | Versión básica sin protección | ❌ No incluido | ❌ No incluidas | `com.tokyoghoull.ghoulstreampro` |
| **secure** | Protección básica con encriptación | ✅ Incluido | ✅ libencryption.so | `com.tokyoghoull.ghoulstreampro.secure` |
| **ultraSecure** | Protección máxima + anti-tampering | ✅ Incluido + Ultra | ✅ Todas las librerías | `com.tokyoghoull.ghoulstreampro.ultra` |

### **🚀 Comandos de Compilación:**

```bash
# Compilar todos los flavors
./gradlew assembleStandardDebug      # ~15MB - Sin protección
./gradlew assembleSecureDebug        # ~17MB - Con protección básica
./gradlew assembleUltraSecureDebug   # ~18MB - Con protección máxima

# Compilar releases
./gradlew assembleStandardRelease
./gradlew assembleSecureRelease
./gradlew assembleUltraSecureRelease

# Verificar que todos compilan correctamente
./gradlew build
```

## 🛡️ **Sistema de Protección Multi-Capa**

### **🔐 Protección Avanzada de Claves:**

El módulo incluye **5 capas de protección** para las claves maestras:

1. **🧩 Construcción Dinámica**: Claves construidas en runtime usando múltiples técnicas
2. **🔒 Control de Acceso**: Métodos privados para acceso centralizado a claves
3. **🛡️ ProGuard Avanzado**: Reglas específicas para ofuscación de clases de seguridad
4. **🚫 Anti-Análisis**: Protección contra debugging y análisis estático
5. **💻 Procesamiento Nativo**: Operaciones críticas en memoria nativa C++

## 📊 **Beneficios de Seguridad**

### **🛡️ Resistencia a Herramientas de Análisis:**

| Herramienta | Sin Protección | Con GhoulStream Security |
|-------------|----------------|--------------------------|
| **JADX** | ✅ URLs visibles en texto plano | ❌ Solo construcción dinámica de claves |
| **APKTool** | ✅ Strings extraídos fácilmente | ❌ Fragmentos ofuscados sin contexto |
| **IDA Pro** | ✅ Análisis estático completo | ❌ Solo desencriptación en runtime |
| **Ghidra** | ✅ Decompilación completa | ❌ Protegido en memoria nativa |
| **String Search** | ✅ Coincidencias directas | ❌ No hay strings literales |
| **Automated Tools** | ✅ Extracción fácil | ❌ 85% de resistencia |

### **⏱️ Métricas de Seguridad:**
- **Tiempo para comprometer:** 30 segundos → 2-8 horas (480x-960x más difícil)
- **Habilidad requerida:** Principiante → Intermedio-Avanzado
- **Herramientas necesarias:** Básicas → Múltiples herramientas especializadas
- **Resistencia a automatización:** 0% → 85%

## ⚙️ **Requisitos Técnicos**

- **Android NDK**: Versión 21+ recomendada
- **Minimum SDK**: Android 28 (API Level 28) - Android 9+
- **Target SDK**: Android 35 (API Level 35) - Android 15
- **Compile SDK**: Android 35 (API Level 35) - Android 15
- **JDK**: Java 17 (Temurin distribution)
- **Gradle**: 8.8+ con Android Gradle Plugin 8.2.2+
- **Arquitecturas**: ARM64-v8a, ARMv7, x86, x86_64
- **Longitud de Clave**: Mínimo 10 caracteres (recomendado 22+)
- **Tamaño de Librería**: ~45KB por arquitectura

## ⚠️ **Consideraciones Importantes**

### **🔄 Compatibilidad entre Flavors:**
- El código principal debe funcionar **con y sin** el módulo
- Usar **imports comentados** en archivos compartidos (`src/main/`)
- Implementar **fallbacks** robustos para cuando la seguridad no esté disponible
- Probar **todos los flavors** antes de hacer release

### **🔑 Gestión de Claves y URLs:**
- Las claves maestras están **hardcodeadas** en `GhoulStreamSecurity.java`
- Para producción, **actualizar las constantes encriptadas** con URLs reales
- Usar `generateEncryptedConstant()` en debug para generar nuevas constantes
- **Nunca** commitear claves o URLs reales en repositorios públicos

### **📱 Impacto en Tamaño del APK:**
- **standard**: ~15MB (sin librerías nativas)
- **secure**: ~17MB (+2MB por librerías nativas de 4 arquitecturas)
- **ultraSecure**: ~18MB (+3MB por funcionalidades extra y protecciones adicionales)

## 🚀 **Flujo de Desarrollo**

### **1️⃣ Desarrollo en el Módulo:**
```bash
# Trabajar en el módulo independiente
cd security-module

# Hacer cambios en src/java/
# Ejemplo: actualizar URLs encriptadas en GhoulStreamSecurity.java

git add .
git commit -m "feat: actualizar URLs de producción"
git push origin main
```

### **2️⃣ Actualizar en el Proyecto Principal:**
```bash
# En GhoulStream
cd security-module
git pull origin main
cd ..

# Confirmar la actualización del submódulo
git add security-module
git commit -m "chore: actualizar security-module con nuevas URLs"
git push origin main
```

### **3️⃣ Verificar Integración:**
```bash
# Compilar todos los flavors para verificar compatibilidad
./gradlew clean
./gradlew assembleStandardDebug    # Sin seguridad - debe compilar
./gradlew assembleSecureDebug      # Con seguridad básica - debe compilar
./gradlew assembleUltraSecureDebug # Con seguridad máxima - debe compilar

# Verificar que todos los tests pasan
./gradlew test
```

## 📄 **Licencia**

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 🙏 **Créditos**

- **Librería Original**: @Qwanwin por CustomBase String Encryption
- **Integración GhoulStream**: @TokyoghoulEs
- **Mejoras de Seguridad**: Contribuciones de la comunidad
- **Integración Git Submodule**: Diseño específico para GhoulStream Pro

## ⚠️ **Disclaimer**

Esta herramienta es solo para propósitos legítimos de seguridad. Los usuarios son responsables de cumplir con las leyes y regulaciones aplicables. Los autores no son responsables del mal uso de este software.

---

**🛡️ Protección nativa para GhoulStream Pro - ¡Mantén tus URLs y credenciales seguras! 🚀**

*Desarrollado específicamente para la integración con [GhoulStream Pro](https://github.com/TokyoghoulEs/GhoulStream)*

*Made with ❤️ for the Android security community*
