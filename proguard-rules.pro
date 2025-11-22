# 🛡️ GhoulStream Security Module - ProGuard Rules
# Reglas de ofuscación para el módulo de seguridad

# Mantener todas las clases del módulo
-keep class com.tokyoghoull.ghoulstream.security.** { *; }

# Mantener métodos nativos (JNI)
-keepclasseswithmembernames class * {
    native <methods>;
}

# No advertir sobre librerías nativas
-dontwarn com.tokyoghoull.ghoulstream.security.**
