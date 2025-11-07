# 🛡️ Mejores Prácticas - GhoulStream Security Module

## Índice

1. [Uso Correcto del Módulo](#uso-correcto-del-módulo)
2. [Gestión de Claves](#gestión-de-claves)
3. [Manejo de Errores](#manejo-de-errores)
4. [Seguridad en Producción](#seguridad-en-producción)
5. [Testing y Validación](#testing-y-validación)
6. [Actualización del Módulo](#actualización-del-módulo)
7. [Troubleshooting](#troubleshooting)

---

## Uso Correcto del Módulo

### ✅ DO: Usar SecurityManagerWrapper en el código principal

**Correcto**:
```java
// En MainActivity.java o cualquier Activity
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ✅ Inicializar el wrapper
        SecurityManagerWrapper.initialize(this);
        
        // ✅ Obtener URLs (funciona en todos los flavors)
        String channelsUrl = SecurityManagerWrapper.getDefaultChannelsUrl();
        
        // ✅ El wrapper maneja automáticamente los fallbacks
        if (channelsUrl != null) {
            loadPlaylist(channelsUrl);
        }
    }
}
```

**Por qué**: El `SecurityManagerWrapper` usa reflexión y proporciona fallbacks automáticos, haciendo que tu código funcione en todos los flavors (standard, secure, ultraSecure).

### ❌ DON'T: Llamar directamente a GhoulStreamSecurity desde código compartido

**Incorrecto**:
```java
// ❌ MAL - Esto solo funciona en flavors secure/ultraSecure
import com.tokyoghoull.ghoulstream.security.GhoulStreamSecurity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ❌ Esto crasheará en flavor standard
        String channelsUrl = GhoulStreamSecurity.getDefaultChannelsUrl();
    }
}
```

**Por qué**: El import fallará en el flavor standard porque la clase no existe.

---

## Gestión de Claves

### ✅ DO: Usar la clave maestra para datos del sistema

**Correcto**:
```java
// Encriptar configuración de usuario
String config = "{\"theme\":\"dark\",\"language\":\"es\"}";
String encrypted = SecurityManagerWrapper.encryptUserConfiguration(config);

// Guardar en SharedPreferences
SharedPreferences prefs = getSharedPreferences("app_config", MODE_PRIVATE);
prefs.edit().putString("user_config", encrypted).apply();

// Recuperar y desencriptar
String encryptedConfig = prefs.getString("user_config", null);
String decrypted = SecurityManagerWrapper.decryptUserConfiguration(encryptedConfig);
```

**Por qué**: La clave maestra está diseñada para datos del sistema y configuraciones.

### ✅ DO: Usar claves personalizadas para datos muy sensibles

**Correcto**:
```java
// Para datos MUY sensibles, puedes usar una clave derivada del usuario
String userPassword = "contraseñaDelUsuario123";
String derivedKey = hashSHA256(userPassword + "GhoulStreamSalt");

// Encriptar con clave personalizada
String sensitiveData = "datos muy sensibles";
String encrypted = NativeEncryption.encrypt(sensitiveData, derivedKey);
```

**Por qué**: Datos muy sensibles pueden beneficiarse de claves únicas por usuario.

### ❌ DON'T: Hardcodear claves personalizadas en el código

**Incorrecto**:
```java
// ❌ MAL - Clave hardcodeada visible
String myKey = "MiClaveSecreta123";
String encrypted = NativeEncryption.encrypt(data, myKey);
```

**Por qué**: Esto anula el propósito de la seguridad. Si necesitas una clave personalizada, deríbala del usuario o del dispositivo.

### ❌ DON'T: Usar claves muy cortas

**Incorrecto**:
```java
// ❌ MAL - Clave muy corta (menos de 10 caracteres)
String encrypted = NativeEncryption.encrypt(data, "abc123");
// Resultado: null (validación falla)
```

**Por qué**: La validación requiere mínimo 10 caracteres para garantizar seguridad básica.

---

## Manejo de Errores

### ✅ DO: Verificar siempre los resultados de encriptación/desencriptación

**Correcto**:
```java
String url = "https://mi-servidor.com/playlist.m3u";
String encrypted = SecurityManagerWrapper.encryptCustomUrl(url);

if (encrypted != null) {
    // ✅ Encriptación exitosa
    saveToDatabase(encrypted);
} else {
    // ❌ Error en encriptación
    Log.e(TAG, "Error encriptando URL, guardando sin encriptar");
    saveToDatabase(url);  // Fallback
}
```

**Por qué**: Los métodos pueden devolver `null` si hay errores. Siempre verifica el resultado.

### ✅ DO: Implementar fallbacks robustos

**Correcto**:
```java
String channelsUrl = SecurityManagerWrapper.getDefaultChannelsUrl();

if (channelsUrl == null || channelsUrl.isEmpty()) {
    // ✅ Fallback a URL hardcodeada
    channelsUrl = "https://fallback-server.com/channels.m3u";
    Log.w(TAG, "Usando URL de fallback");
}

loadPlaylist(channelsUrl);
```

**Por qué**: Tu app debe funcionar incluso si el módulo de seguridad falla.

### ❌ DON'T: Asumir que la encriptación siempre funciona

**Incorrecto**:
```java
// ❌ MAL - No verifica el resultado
String encrypted = SecurityManagerWrapper.encryptCustomUrl(url);
saveToDatabase(encrypted);  // Puede ser null!
```

**Por qué**: Si `encrypted` es `null`, guardarás un valor inválido en la base de datos.

### ❌ DON'T: Crashear la app si el módulo no está disponible

**Incorrecto**:
```java
// ❌ MAL - Crashea si el módulo no está disponible
if (!SecurityManagerWrapper.isSecurityAvailable()) {
    throw new RuntimeException("Módulo de seguridad no disponible");
}
```

**Por qué**: El flavor standard no tiene el módulo. La app debe funcionar con fallbacks.

---

## Seguridad en Producción

### ✅ DO: Habilitar ProGuard/R8 en builds de Release

**Correcto** (en `app/build.gradle`):
```gradle
buildTypes {
    release {
        minifyEnabled true  // ✅ Habilitar R8
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 
                      'proguard-rules.pro'
    }
}
```

**Por qué**: ProGuard/R8 ofusca el código adicional, aumentando la seguridad de 60% a 85%.

### ✅ DO: Usar el flavor correcto según el nivel de seguridad requerido

**Correcto**:
```bash
# Para desarrollo y testing
./gradlew assembleStandardDebug

# Para producción con seguridad básica
./gradlew assembleSecureRelease

# Para producción con máxima seguridad
./gradlew assembleUltraSecureRelease
```

**Por qué**: Cada flavor tiene un nivel de seguridad diferente. Elige el apropiado.

### ✅ DO: Actualizar las URLs encriptadas antes de release

**Correcto**:
```java
// En desarrollo, usa el generador para crear nuevas constantes
if (BuildConfig.DEBUG) {
    String newUrl = "https://production-server.com/channels.m3u";
    String encrypted = GhoulStreamSecurity.generateEncryptedConstant(newUrl);
    Log.i(TAG, "Nueva constante encriptada: " + encrypted);
}
```

**Por qué**: Las URLs de ejemplo deben reemplazarse con URLs reales de producción.

### ❌ DON'T: Dejar URLs de ejemplo en producción

**Incorrecto**:
```java
// ❌ MAL - URLs de ejemplo en producción
private static final String ENCRYPTED_CHANNELS_URL = "gH9sP3wM7qR5tV8yB2cF5jL9nQ2sU6xZ0aC4fH8kM1pT4vY7zB0eG3iL6oR9uX2w";
```

**Por qué**: Estas son URLs de ejemplo. Debes reemplazarlas con tus URLs reales encriptadas.

### ❌ DON'T: Deshabilitar la validación de inputs

**Incorrecto**:
```java
// ❌ MAL - Comentar la validación
// if (!validateInputs(text, key)) {
//     return null;
// }
```

**Por qué**: La validación previene errores y garantiza seguridad mínima.

---

## Testing y Validación

### ✅ DO: Probar todos los flavors antes de release

**Correcto**:
```bash
# Compilar y probar todos los flavors
./gradlew clean
./gradlew assembleStandardDebug
./gradlew assembleSecureDebug
./gradlew assembleUltraSecureDebug

# Instalar y probar en dispositivo
adb install app/build/outputs/apk/standard/debug/app-standard-debug.apk
adb install app/build/outputs/apk/secure/debug/app-secure-debug.apk
adb install app/build/outputs/apk/ultraSecure/debug/app-ultraSecure-debug.apk
```

**Por qué**: Cada flavor tiene comportamiento diferente. Debes probar todos.

### ✅ DO: Usar el sistema de diagnósticos

**Correcto**:
```java
// Obtener diagnósticos completos
String diagnostics = SecurityManagerWrapper.getDiagnostics();
Log.i(TAG, diagnostics);

// Verificar disponibilidad
if (SecurityManagerWrapper.isSecurityAvailable()) {
    Log.i(TAG, "✅ Módulo de seguridad disponible");
} else {
    Log.w(TAG, "⚠️ Módulo de seguridad no disponible - usando fallbacks");
}
```

**Por qué**: Los diagnósticos te ayudan a identificar problemas rápidamente.

### ✅ DO: Verificar logs durante desarrollo

**Correcto**:
```bash
# Ver logs del módulo de seguridad
adb logcat | grep -i "Security"

# Logs esperados en flavor secure:
# I/NativeEncryption: ✅ Librería nativa cargada exitosamente
# I/GhoulStreamSecurity: ✅ GhoulStream Security Module inicializado
# I/SecurityManagerWrapper: ✅ Módulo de seguridad inicializado exitosamente
```

**Por qué**: Los logs te indican si el módulo se inicializó correctamente.

### ❌ DON'T: Ignorar errores en los logs

**Incorrecto**:
```bash
# ❌ Ignorar estos errores:
E/NativeEncryption: ❌ Error cargando librería nativa
E/SecurityManagerWrapper: ❌ Error inicializando módulo de seguridad
```

**Por qué**: Estos errores indican que el módulo no funciona. Investiga la causa.

---

## Actualización del Módulo

### ✅ DO: Actualizar el submódulo correctamente

**Correcto**:
```bash
# En el proyecto principal GhoulStream
cd security-module
git pull origin main
cd ..

# Confirmar la actualización
git add security-module
git commit -m "chore: actualizar security-module a v1.1.0"
git push
```

**Por qué**: El submódulo es un repositorio Git independiente. Debes actualizarlo explícitamente.

### ✅ DO: Verificar compatibilidad después de actualizar

**Correcto**:
```bash
# Después de actualizar el módulo
./gradlew clean
./gradlew build

# Verificar que todos los flavors compilan
./gradlew assembleStandardDebug
./gradlew assembleSecureDebug
./gradlew assembleUltraSecureDebug
```

**Por qué**: Las actualizaciones pueden introducir cambios incompatibles.

### ❌ DON'T: Modificar archivos del módulo directamente en el proyecto principal

**Incorrecto**:
```bash
# ❌ MAL - Editar archivos del submódulo directamente
nano security-module/src/java/.../GhoulStreamSecurity.java
```

**Por qué**: Los cambios se perderán cuando actualices el submódulo. Edita en el repositorio del módulo.

---

## Troubleshooting

### Problema: "Librería nativa no disponible"

**Síntoma**:
```
E/NativeEncryption: ❌ Error cargando librería nativa: java.lang.UnsatisfiedLinkError
```

**Solución**:
1. Verifica que las librerías .so están en `security-module/libs/`
2. Verifica que el `sourceSets` en `build.gradle` incluye `../security-module/libs`
3. Limpia y recompila: `./gradlew clean build`

### Problema: "Clases del módulo no encontradas"

**Síntoma**:
```
W/SecurityManagerWrapper: ⚠️ Clases del módulo de seguridad no encontradas
```

**Solución**:
1. Verifica que el submódulo está inicializado: `git submodule update --init --recursive`
2. Verifica que `settings.gradle` incluye `:security-module`
3. Verifica que el `sourceSets` en `build.gradle` incluye `../security-module/src/java`

### Problema: "Encriptación devuelve null"

**Síntoma**:
```java
String encrypted = SecurityManagerWrapper.encryptCustomUrl(url);
// encrypted es null
```

**Solución**:
1. Verifica los logs: `adb logcat | grep -i "Security"`
2. Verifica que la URL no es null o vacía
3. Verifica que el módulo está disponible: `SecurityManagerWrapper.isSecurityAvailable()`

### Problema: "APK muy grande"

**Síntoma**:
APK de 25+ MB en flavor secure/ultraSecure

**Solución**:
1. Habilita R8 en release: `minifyEnabled true`
2. Habilita shrinkResources: `shrinkResources true`
3. Usa App Bundles en lugar de APK: `./gradlew bundleRelease`

---

## Checklist de Seguridad para Release

Antes de hacer release, verifica:

- [ ] ✅ ProGuard/R8 habilitado en release builds
- [ ] ✅ URLs de ejemplo reemplazadas con URLs reales encriptadas
- [ ] ✅ Todos los flavors compilan correctamente
- [ ] ✅ Testing en dispositivos reales (ARM64, ARMv7)
- [ ] ✅ Logs de seguridad verificados
- [ ] ✅ Diagnósticos muestran "✅ PASS"
- [ ] ✅ Fallbacks funcionan en flavor standard
- [ ] ✅ Submódulo actualizado a última versión
- [ ] ✅ Documentación actualizada
- [ ] ✅ GitHub Actions compila las 6 variantes

---

## Recursos Adicionales

- **Documentación de Seguridad**: `SECURITY_DOCUMENTATION.md`
- **Guía de Integración**: `../GhoulStream/docs/security-module-integration.md`
- **Guía de Testing**: `../GhoulStream/docs/security-module-testing.md`
- **README del Módulo**: `README.md`

---

**Última actualización**: 2025-01-07
**Versión del módulo**: 1.0.0
**Autor**: @TokyoghoulEs
