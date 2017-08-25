-dontwarn java.lang.invoke.*

#Models
-keep class com.popalay.cardme.data.models.** { *; }

# Remove logs
-assumenosideeffects class android.util.Log {
   public static boolean isLoggable(java.lang.String, int);
   public static int v(...);
   public static int i(...);
   public static int w(...);
   public static int d(...);
   public static int e(...);
}

-dontwarn com.google.errorprone.annotations.*