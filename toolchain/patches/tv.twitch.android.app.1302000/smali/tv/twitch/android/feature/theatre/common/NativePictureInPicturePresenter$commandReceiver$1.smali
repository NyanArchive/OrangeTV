diff --git a/smali_classes5/tv/twitch/android/feature/theatre/common/NativePictureInPicturePresenter$commandReceiver$1.smali b/smali_classes5/tv/twitch/android/feature/theatre/common/NativePictureInPicturePresenter$commandReceiver$1.smali
--- a/smali_classes5/tv/twitch/android/feature/theatre/common/NativePictureInPicturePresenter$commandReceiver$1.smali
+++ b/smali_classes5/tv/twitch/android/feature/theatre/common/NativePictureInPicturePresenter$commandReceiver$1.smali
@@ -54,18 +54,18 @@
 
     move-result p2
 
-    const v0, -0x69bc8593
+    const v0, -0x53864877
 
     if-eq p2, v0, :cond_3
 
-    const v0, -0xf2323c
+    const v0, 0x20cd7d28
 
     if-eq p2, v0, :cond_1
 
     goto :goto_1
 
     :cond_1
-    const-string p2, "tv.twitch.android.media.action.pause_playback"
+    const-string p2, "tv.orange.action.pause_playback"
 
     invoke-virtual {p1, p2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
 
@@ -90,7 +90,7 @@
     goto :goto_1
 
     :cond_3
-    const-string p2, "tv.twitch.android.media.action.resume_playback"
+    const-string p2, "tv.orange.action.resume_playback"
 
     .line 137
     invoke-virtual {p1, p2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
