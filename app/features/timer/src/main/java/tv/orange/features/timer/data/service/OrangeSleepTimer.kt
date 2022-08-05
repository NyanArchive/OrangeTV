package tv.orange.features.timer.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import java.util.concurrent.TimeUnit

class OrangeSleepTimer : Service() {
    private var timer: CountDownTimer? = null

    private var tick = 0

    private val builder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setSmallIcon(ResourceManager.get().getDrawableId("ic_twitch_glitch_uv_alpha_only"))
            .setContentTitle(ResourceManager.get().getString("orange_timer_title"))
            .setColor(
                ContextCompat.getColor(
                    baseContext,
                    ResourceManager.get().getColorId("twitch_purple")
                )
            )
            .setAutoCancel(false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .addAction(
                ResourceManager.get().getDrawableId("ic_cancel"),
                ResourceManager.get().getString("orange_timer_cancel"),
                PendingIntent.getService(baseContext, 1, createStopServiceIntent(baseContext), 0)
            ).addAction(
                ResourceManager.get().getDrawableId("ic_add"), "+5",
                PendingIntent.getService(
                    baseContext, 2, createAddTimeIntent(
                        context = baseContext,
                        seconds = 5 * 60
                    ), 0
                )
            ).addAction(
                ResourceManager.get().getDrawableId("ic_add"), "+15",
                PendingIntent.getService(
                    baseContext, 3, createAddTimeIntent(
                        context = baseContext,
                        seconds = 15 * 60
                    ), 0
                )
            ).setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.action?.let {
            when (it) {
                ACTION_ADD -> addTime(seconds = intent.getIntExtra(EXTRA_SECONDS, 0))
                ACTION_CANCEL -> cancel()
                ACTION_START -> start(
                    duration = intent.getIntExtra(EXTRA_SECONDS, 0)
                )
                else -> error("Unknown action: $it")
            }
        }

        return START_NOT_STICKY
    }

    private fun start(duration: Int) {
        timer?.cancel()

        timer = object : CountDownTimer(
            TimeUnit.SECONDS.toMillis(duration.toLong()),
            TimeUnit.SECONDS.toMillis(1)
        ) {
            override fun onTick(millisUntilFinished: Long) {
                tick(seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toInt())
            }

            override fun onFinish() {
                cancel()
                Core.killApp()
            }
        }.apply {
            start()
        }
    }

    private fun tick(seconds: Int) {
        tick = seconds
        updateNotificationTime(seconds = seconds)
    }

    private fun updateNotificationTime(seconds: Int) {
        builder.setContentText(DateUtils.formatElapsedTime(seconds.toLong()))
        NotificationManagerCompat.from(baseContext).notify(TIMER_ID, builder.build())
    }

    private fun cancel() {
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun addTime(seconds: Int) {
        tick += seconds
        start(duration = tick)
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(TIMER_ID, builder.build())
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val CHANNEL_ID = "OrangeTV"
        private const val TIMER_ID = 799

        private const val ACTION_CANCEL = "tv.orange.service.action.CANCEL"
        private const val ACTION_ADD = "tv.orange.service.action.ADD"
        private const val ACTION_START = "tv.orange.service.action.START"

        private const val EXTRA_SECONDS = "tv.orange.service.extra.SECONDS"

        fun startService(context: Context, seconds: Int) {
            createNotificationChannel(context)
            context.startService(Intent(context, OrangeSleepTimer::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_SECONDS, seconds)
            })
        }

        private fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    ResourceManager.get().getString("orange_notification_channel_name"),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description =
                        ResourceManager.get().getString("orange_notification_channel_desc")
                }
                context.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }
        }

        fun createAddTimeIntent(context: Context?, seconds: Int): Intent {
            return Intent(context, OrangeSleepTimer::class.java).apply {
                action = ACTION_ADD
                putExtra(EXTRA_SECONDS, seconds)
            }
        }

        fun createStopServiceIntent(context: Context?): Intent {
            return Intent(context, OrangeSleepTimer::class.java).apply {
                action = ACTION_CANCEL
            }
        }
    }
}