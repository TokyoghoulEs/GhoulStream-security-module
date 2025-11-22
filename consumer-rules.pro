# 🛡️ GhoulStream Security Module - Consumer ProGuard Rules
# Estas reglas se aplican automáticamente a la app que usa este módulo
# Compatible con las reglas de la app principal (r8-rules.pro)

# ========================================
# 🔐 MÓDULO DE SEGURIDAD
# ========================================

# Mantener todas las clases públicas del módulo de seguridad
-keep public class com.tokyoghoull.ghoulstream.security.** {
    public *;
}

# Mantener métodos nativos (JNI para libencryption.so)
-keepclasseswithmembernames class * {
    native <methods>;
}

# Mantener interfaces públicas
-keep interface com.tokyoghoull.ghoulstream.security.** {
    *;
}

# No ofuscar nombres de clases de seguridad (para debugging)
-keepnames class com.tokyoghoull.ghoulstream.security.**

# ========================================
# 🔧 ATRIBUTOS NECESARIOS
# ========================================

# Mantener anotaciones
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Mantener información de línea para stack traces
-keepattributes SourceFile,LineNumberTable

# ========================================
# 🚫 WARNINGS SUPPRESSION
# ========================================

# No advertir sobre clases del módulo de seguridad
-dontwarn com.tokyoghoull.ghoulstream.security.**
