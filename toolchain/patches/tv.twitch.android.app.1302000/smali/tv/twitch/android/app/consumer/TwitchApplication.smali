diff --git a/smali_classes4/tv/twitch/android/app/consumer/TwitchApplication.smali b/smali_classes4/tv/twitch/android/app/consumer/TwitchApplication.smali
--- a/smali_classes4/tv/twitch/android/app/consumer/TwitchApplication.smali
+++ b/smali_classes4/tv/twitch/android/app/consumer/TwitchApplication.smali
@@ -282,9 +282,7 @@
     move-result-object v1
 
     .line 115
-    invoke-virtual {p0}, Landroid/app/Application;->getPackageName()Ljava/lang/String;
-
-    move-result-object v2
+    const-string v2, "tv.twitch.android.app"
 
     const-string v3, "13.2.0"
 
