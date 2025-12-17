# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Room
#-keep class * extends androidx.room.RoomDatabase
#-keep class * extends androidx.room.Entity
#
## Hilt
#-keep class dagger.hilt.** { *; }
#-keep class * extends dagger.hilt.android.internal.** { *; }
#-keep class * extends dagger.hilt.android.HiltAndroidApp { *; }
#
## Firebase
#-keep class com.google.firebase.** { *; }
#-dontwarn com.google.firebase.**
#
## Glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep class * extends com.bumptech.glide.module.AppGlideModule {
#    <init>(...);
#}
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#    **[] $VALUES;
#    public *;
#}
#
## Gson
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#
## OkHttp
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
#-dontwarn okhttp3.**
#
## Retrofit
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class retrofit2.** { *; }
#-keepclasseswithmembers class * {
#    @retrofit2.http.* <methods>;
#}
#-dontwarn retrofit2.**