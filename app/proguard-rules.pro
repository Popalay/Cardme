-dontwarn java.lang.invoke.*

#Models
-keep class com.popalay.yocard.data.models.** { *; }
-keep class com.popalay.yocard.data.events.** { *; }

#RecyclerViewPager
-keep class com.lsjwzh.widget.recyclerviewpager.**
-dontwarn com.lsjwzh.widget.recyclerviewpager.**

# Remove logs
#-assumenosideeffects class android.util.Log {
#   public static boolean isLoggable(java.lang.String, int);
#   public static int v(...);
#   public static int i(...);
#   public static int w(...);
#   public static int d(...);
#   public static int e(...);
#}

# Because Canary Studio sucks
-dontwarn android.support.v7.**
-keep class android.support.v7.widget.** { *; }
-keep class android.support.v7.view.** { *; }

-dontwarn android.support.constraint.**
-keep class android.support.constraint.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.widget.** { *; }
