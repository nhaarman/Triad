-keep,allowshrinking class * extends com.nhaarman.triad.Screen
-keepclasseswithmembers class * {
    public static final com.nhaarman.triad.Screen create();
}