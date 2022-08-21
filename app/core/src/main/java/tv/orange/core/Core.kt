package tv.orange.core

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.*
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.core.models.lifecycle.LifecycleController
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import javax.inject.Inject
import kotlin.system.exitProcess

class Core @Inject constructor(
    val context: Context
) : LifecycleController, LifecycleAware, Feature {
    private val modules = mutableSetOf<LifecycleAware>()

    companion object {
        private lateinit var bridge: Bridge

        @JvmStatic
        fun get() = getFeature(Core::class.java)

        @JvmStatic
        fun setBridge(bridge: Bridge) {
            this.bridge = bridge
        }

        @JvmStatic
        fun <T : Feature> getFeature(clazz: Class<T>): T {
            return getBridge().getFeature(clazz = clazz)
        }

        @JvmStatic
        private fun getBridge(): Bridge {
            return bridge
        }

        @JvmStatic
        fun killApp() {
            exitProcess(0);
        }

        @JvmStatic
        fun restart(activity: Activity) {
            val intent: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            val manager = (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            manager.setExact(
                AlarmManager.ELAPSED_REALTIME,
                1500L,
                PendingIntent.getActivity(
                    activity,
                    0, Intent(
                        activity,
                        activity::class.java
                    ),
                    intent
                )
            )
            killApp()
        }

        fun vibrate(context: Context, delay: Int, duration: Int) {
            Handler(context.mainLooper).postDelayed(
                { intVibrate(context = context, duration = duration) },
                delay.toLong()
            )
        }

        private fun intVibrate(context: Context, duration: Int) {
            val v = getVibrator(context = context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v?.vibrate(
                    VibrationEffect.createOneShot(
                        duration.toLong(),
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                v?.vibrate(duration.toLong())
            }
        }

        private fun getVibrator(context: Context): Vibrator? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?)?.defaultVibrator
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
            }
        }
    }

    fun initialize() {}

    override fun registerLifecycleListeners(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            modules.add(listener)
        }
    }

    override fun unregisterLifecycleListener(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            modules.remove(listener)
        }
    }

    override fun onAllComponentDestroyed() {
        modules.forEach {
            it.onAllComponentDestroyed()
        }
    }

    override fun onAllComponentStopped() {
        modules.forEach {
            it.onAllComponentStopped()
        }
    }

    override fun onSdkResume() {
        modules.forEach {
            it.onSdkResume()
        }
    }

    override fun onAccountLogout() {
        modules.forEach {
            it.onAccountLogout()
        }
    }

    override fun onFirstActivityCreated() {
        modules.forEach {
            it.onFirstActivityCreated()
        }
    }

    override fun onFirstActivityStarted() {
        modules.forEach {
            it.onFirstActivityStarted()
        }
    }

    override fun onConnectedToChannel(channelId: Int) {
        modules.forEach {
            it.onConnectedToChannel(channelId)
        }
    }

    override fun onConnectingToChannel(channelId: Int) {
        modules.forEach {
            it.onConnectingToChannel(channelId)
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {
        Logger.debug("OrangeTV:${BuildConfigUtil.buildConfig.number}")
    }
}