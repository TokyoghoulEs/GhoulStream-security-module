# 🛡️ GhoulStream Security Module - ProGuard Rules
# Reglas de ofuscación para el módulo de seguridad
# Alineadas con r8-rules.pro de la app principal

# ========================================
# 🔐 MÓDULO DE SEGURIDAD
# ========================================

# Mantener todas las clases del módulo (es un módulo pequeño)
-keep class com.tokyoghoull.ghoulstream.security.** { *; }

# ========================================
# 🔧 MÉTODOS NATIVOS (JNI)
# ========================================

# Mantener métodos nativos para libencryption.so
-keepclasseswithmembernames class * {
    native <methods>;
}

# ========================================
# 🔧 ATRIBUTOS NECESARIOS
# ========================================

# Mantener atributos para reflexión y debugging
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes SourceFile,LineNumberTable

# ========================================
# 🚫 WARNINGS SUPPRESSION
# ========================================

# No advertir sobre librerías nativas
-dontwarn com.tokyoghoull.ghoulstream.security.**
