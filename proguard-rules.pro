# Keep only absolute essentials
-keepattributes Signature
-keepattributes *Annotation*

# Keep ONLY the entry point methods, allow class name obfuscation
-keep,allowobfuscation class com.codecatto.kitterhack.Kitterhack {
    public void onInitialize();
}

-keep,allowobfuscation class com.codecatto.kitterhack.KitterhackClient {
    public void onInitializeClient();
}

# Keep native methods (JNI) but obfuscate the class name
-keepclasseswithmembernames,allowobfuscation,includedescriptorclasses class * {
    native <methods>;
}

# Keep Mixin classes but allow obfuscation where possible
-keep,allowobfuscation @org.spongepowered.asm.mixin.Mixin class * {
    <init>();
}

# Keep Mixin injection methods
-keepclassmembers,allowobfuscation class * {
    @org.spongepowered.asm.mixin.injection.Inject *;
    @org.spongepowered.asm.mixin.injection.ModifyVariable *;
    @org.spongepowered.asm.mixin.injection.ModifyArg *;
    @org.spongepowered.asm.mixin.injection.Redirect *;
}

# Flatten package structure (moves everything to root package)
-repackageclasses ''
-allowaccessmodification
-overloadaggressively
-mergeinterfacesaggressively

# Remove debugging info to make reverse engineering harder
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Obfuscate everything else
-dontoptimize
-dontpreverify

# Keep enum methods
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Silence warnings
-dontwarn **
-dontnote **
-ignorewarnings