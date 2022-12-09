package tv.orange.core

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.SingleSource
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.PinnedMessageStrategy
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.core.models.lifecycle.LifecycleController
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import tv.orange.models.abc.TCPProvider
import tv.orange.models.abc.TwitchComponentProvider
import tv.twitch.android.app.core.ApplicationContext
import tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model.CreatorPinnedChatMessageChannelModel
import tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model.CreatorPinnedChatMessageMessageModel
import tv.twitch.android.shared.subscriptions.db.SubscriptionPurchaseEntity
import tv.twitch.android.shared.subscriptions.purchasers.SubscriptionPurchaseResponse
import tv.twitch.android.util.CoreDateUtil
import javax.inject.Inject
import kotlin.system.exitProcess

@AutoInitialize
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
        fun provideTCP(): TwitchComponentProvider {
            return (ApplicationContext.getInstance().getContext() as TCPProvider).provideTCP()
        }

        @JvmStatic
        fun killApp() {
            exitProcess(0);
        }

        @JvmStatic
        fun toast(msg: String) {
            Toast.makeText(get().context, msg, Toast.LENGTH_SHORT).show()
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

        @JvmStatic
        fun <T : Feature> destroyFeature(clazz: Class<T>) {
            getBridge().destroyFeature(clazz)
        }

        @JvmStatic
        fun isPinnedChatMessageEnabled(): Boolean {
            return Flag.PINNED_MESSAGE.asVariant<PinnedMessageStrategy>() != PinnedMessageStrategy.Disabled
        }

        @JvmStatic
        fun hookUnpinnedMS(timeMessageUnpinnedMS: Long?): Long? {
            return when (Flag.PINNED_MESSAGE.asVariant<PinnedMessageStrategy>()) {
                PinnedMessageStrategy.Disabled,
                PinnedMessageStrategy.Default -> timeMessageUnpinnedMS
                PinnedMessageStrategy.SEC30 -> {
                    val calc = CoreDateUtil().currentTimeInMillis + 30 * 1000
                    timeMessageUnpinnedMS?.let { ms ->
                        if (ms - calc <= 0) {
                            ms
                        } else {
                            calc
                        }
                    } ?: calc
                }
            }
        }

        @JvmStatic
        fun injectBilling(
            activity: Activity,
            res: Single<SubscriptionPurchaseResponse>,
            purchaseEntity: SubscriptionPurchaseEntity
        ): Single<SubscriptionPurchaseResponse> {
            return res.doOnSuccess {
                if (it is SubscriptionPurchaseResponse.Success) {
                    openUrl(activity, "https://www.twitch.tv/subs/${purchaseEntity.channelDisplayName}")
                }
            }
        }

        private fun openUrl(context: Context, url: String) {
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
        LoggerImpl.debug("OrangeTV:${BuildConfigUtil.buildConfig.number}")
    }
}