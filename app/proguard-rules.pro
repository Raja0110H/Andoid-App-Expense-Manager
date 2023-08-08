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


-keep class com.expense.manager.Model.** { *; }

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.view.View {
      public <init>(android.content.Context);
      public <init>(android.content.Context, android.util.AttributeSet);
      public <init>(android.content.Context, android.util.AttributeSet, int);
      public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class androidx.lifecycle.** {*;}
-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-keepclassmembers class * implements java.io.Serializable {
    static ** CREATOR;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**


-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,
LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-keep public class com.startapp.* { public *; }
-dontwarn com.startapp.**
-keepattributes *Annotation*

-keep public class com.google.ads.** {*;}
-keep public class com.google.android.gms.** {*;}
-keep public class com.tappx.** { *; }
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keepnames class org.apache.** {*;}
-keep public class org.apache.** {*;}
-dontwarn org.apache.http.**
-dontwarn com.squareup.picasso.**
-keep class io.agora.**{*;}
-keep class com.wang.avi.**{*;}
-keep public class com.facebook.ads.* { public *; }
-dontwarn com.facebook.ads.**


-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep class com.android.billingclient.** {*; }

-repackageclasses com.cashfy.budgetexpensemanager.chopdo
-allowaccessmodification

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
