package tv.orange.core.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import tv.orange.core.ResourceManager

object NotificationUtil {
    const val CHANNEL_ID = "PurpleTV"
    const val UPDATER_ID = 700

    @JvmStatic
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                ResourceManager.get().getString("orangetv_notification_channel_name"),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = ResourceManager.get().getString("orangetv_notification_channel_desc")
            }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}