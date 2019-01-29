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

#-ignorewarnings
#######***********声网**********
-keep class io.agora.**{*;}
#######***********声网**********

#######***********百度地图**********
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**
#######***********百度地图**********

#######***********retrofit2**********
# Retrofit does reflection on generic parameters and InnerClass is required to use Signature.
-keepattributes Signature, InnerClasses

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.-KotlinExtensions


#######***********retrofit2**********

#######***********okhttp3**********
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
#######***********okhttp3**********

#######***********glide**********
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#######***********glide**********

#######***********bean**********
-keep class com.lwt.qmqiu.bean** {*;}
#######***********bean**********

#######***********bugly  up**********
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
#######***********bugly up**********

#不混淆资源类
 -keep class **.R$* {*;}
 -keep class **.R{*;}
 -keepclassmembers class **.R$* {
     public static <fields>;
 }
 -dontwarn **.R$*

 #保持 Serializable 不被混淆并且enum 类也不被混淆
 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     !private <fields>;
     !private <methods>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

 -keepclasseswithmembernames class * {
         native <methods>;
     }

 -keep public class * extends android.view.View {
         public <init>(android.content.Context);
         public <init>(android.content.Context, android.util.AttributeSet);
         public <init>(android.content.Context, android.util.AttributeSet, int);
         public void set*(...);
     }

 #保持自定义控件类不被混淆
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

 #保持自定义控件类不被混淆
 -keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }

 #保持 Parcelable 不被混淆
 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

 #保持 Serializable 不被混淆
 -keepnames class * implements java.io.Serializable


 # Gson
 -keep class com.google.gson.stream.** { *; }
 -keepattributes EnclosingMethod
 -keep class org.xz_sale.entity.**{*;}

 #package android.support.design.widget; tab底下的横线长短利用反射缩短的,不能被混淆
  -keep class android.support.design.widget.TabLayout {*;}

 # RxJava RxAndroid
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
     long producerIndex;
     long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 #banner
 -keep class com.youth.banner.** {
     *;
  }
#picasso
  -dontwarn com.squareup.picasso.**
#greendao
  -keep class org.greenrobot.greendao.**{*;}
  -keep public interface org.greenrobot.greendao.**
  -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
  public static java.lang.String TABLENAME;
  }
  -keep class **$Properties
  -keep class net.sqlcipher.database.**{*;}
  -keep public interface net.sqlcipher.database.**
  -dontwarn net.sqlcipher.database.**
  -dontwarn org.greenrobot.greendao.**

  #phoenix
  -keep class com.guoxiaoxing.phoenix.picker.** {*;}
  #mimemg
-keep class com.xiaomi.ad.**{*;}
-keep class com.miui.zeus.**{*;}
