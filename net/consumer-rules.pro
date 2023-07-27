# App代码混淆
-keep class cn.hades.**{*;}
-dontwarn cn.hades.**

# Gson
-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** {*;}
-keep interface okhttp3.** {*;}
-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit2
-keep,allowobfuscation,allowshrinking class io.reactivex.Flowable
-keep,allowobfuscation,allowshrinking class io.reactivex.Maybe
-keep,allowobfuscation,allowshrinking class io.reactivex.Observable
-keep,allowobfuscation,allowshrinking class io.reactivex.Single

# 下载组件
-keep class com.liulishuo.okdownload.**{*;}
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings