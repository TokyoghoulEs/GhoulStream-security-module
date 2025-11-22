# 🛡️ GhoulStream Security Module - Consumer ProGuard Rules
# Estas reglas se aplican automáticamente a la app que usa este módulo

# Mantener todas las clases públicas del módulo de seguridad
-keep public class com.tokyoghoull.ghoulstream.security.** {
    public *;
}

# Mantener métodos nativos
-keepclasseswithmembernames class * {
    native <methods>;
}

# Mantener interfaces públicas
-keep interface com.tokyoghoull.ghoulstream.security.** {
    *;
}

# No ofuscar nombres de clases de seguridad (para debugging)
-keepnames class com.tokyoghoull.ghoulstream.security.**

# Mantener anotaciones
-keepattributes *Annotation*

# Mantener información de línea para stack traces
-keepattributes SourceFile,LineNumberTable
