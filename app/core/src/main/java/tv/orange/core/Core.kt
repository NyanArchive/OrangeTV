package tv.orange.core

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.widget.Toast
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.core.models.lifecycle.LifecycleController
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import javax.inject.Inject
import kotlin.system.exitProcess


@AutoInitialize
class Core @Inject constructor(
    val context: Context
) : LifecycleController, LifecycleAware, Feature {
    private val lifecycleModules = mutableSetOf<LifecycleAware>()

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
            return bridge.getFeature(clazz = clazz)
        }

        @JvmStatic
        fun killApp() {
            exitProcess(0);
        }

        @JvmStatic
        fun showToast(msg: String) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    get().context,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        @JvmStatic
        fun restart(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                newRestartMethod(activity)
            } else {
                oldRestartMethod(activity)
            }

            killApp()
        }

        private fun oldRestartMethod(activity: Activity) {
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
        }

        private fun newRestartMethod(activity: Activity) {
            val pm = activity.baseContext.packageManager
            val pn = activity.baseContext.packageName
            pm.getLaunchIntentForPackage(pn)?.let { intent: Intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            }
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

        @JvmStatic
        fun openUrl(context: Context, url: String) {
            if (url.isBlank()) {
                return
            }

            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun registerLifecycleListeners(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            lifecycleModules.add(listener)
        }
    }

    override fun unregisterLifecycleListener(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            lifecycleModules.remove(listener)
        }
    }

    override fun onAllComponentDestroyed() {
        lifecycleModules.forEach {
            it.onAllComponentDestroyed()
        }
    }

    override fun onAllComponentStopped() {
        lifecycleModules.forEach {
            it.onAllComponentStopped()
        }
    }

    override fun onSdkResume() {
        lifecycleModules.forEach {
            it.onSdkResume()
        }
    }

    override fun onAccountLogout() {
        lifecycleModules.forEach {
            it.onAccountLogout()
        }
    }

    override fun onFirstActivityCreated() {
        lifecycleModules.forEach {
            it.onFirstActivityCreated()
        }
    }

    override fun onFirstActivityStarted() {
        lifecycleModules.forEach {
            it.onFirstActivityStarted()
        }
    }

    override fun onConnectedToChannel(channelId: Int) {
        lifecycleModules.forEach {
            it.onConnectedToChannel(channelId)
        }
    }

    override fun onConnectingToChannel(channelId: Int) {
        lifecycleModules.forEach {
            it.onConnectingToChannel(channelId)
        }
    }

    override fun onCreateFeature() {
        LoggerImpl.info("PurpleTV:${BuildConfigUtil.buildConfig.number}")
    }
}