package tv.orange.core

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.Signature
import android.net.Uri
import android.os.*
import android.widget.Toast
import com.google.android.exoplayer2.PlaybackParameters
import io.reactivex.Single
import tv.orange.core.compat.ClassCompat
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asIntRange
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.PinnedMessageStrategy
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.core.models.lifecycle.LifecycleController
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.player.TwitchPlayerProvider
import tv.twitch.android.shared.subscriptions.db.SubscriptionPurchaseEntity
import tv.twitch.android.shared.subscriptions.purchasers.SubscriptionPurchaseResponse
import tv.twitch.android.util.CoreDateUtil
import javax.inject.Inject
import kotlin.system.exitProcess


@AutoInitialize
class Core @Inject constructor(
    val context: Context
) : LifecycleController, LifecycleAware, Feature {
    private val lifecycleModules = mutableSetOf<LifecycleAware>()

    companion object {
        private val MOD_SIGNATURE = arrayOf(
            Signature(
                "30820229308201920209009f8262da41668786300d06092a864886f70d01010505003058310b3009060355040613025255311330110603550408130a536f6d652d53746174653121301f060355040a1318496e7465726e6574205769646769747320507479204c74643111300f060355040313086e6f70627265616b3020170d3230303332313037333530315a180f32303531303931343037333530315a3058310b3009060355040613025255311330110603550408130a536f6d652d53746174653121301f060355040a1318496e7465726e6574205769646769747320507479204c74643111300f060355040313086e6f70627265616b30819f300d06092a864886f70d010101050003818d0030818902818100b946015177bce69f915994ebc2d0e1dce92eb66ada5f68c06ab5e9e7411684aabef3f22477519f9a025ecb144a948c4d4708bc98fe05ff9138db6af0540b5534e4815786b265d7bb8383f381aa2614174e7bb7631c49c04fb133f45bc3a3b95ed8e128f322a802a042a0b36763ae747fd25073eba4df7ea11b856a68a3d607470203010001300d06092a864886f70d0101050500038181005dc50d71e3d506a9ef72f486d5f3ca125228124505d4f8766f2180c0626f918c2044872802e9e8e1619291c0d06074112999e7139553f621bd010a76c74fa889994415bcc21cd8af73e0e390a5d4d02b4551119a97419e8fc199ac03c083e6b0bfdef3c5cc05c920a01401be75ac49554311b71e0474080d21605eb1bda97990"
            )
        )
        private val TWITCH_SIGNATURE = arrayOf(
            Signature(
                "30820255308201bea003020102020455428ee9300d06092a864886f70d0101050500306f310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d53616e204672616e636973636f310f300d060355040a1306547769746368310f300d060355040b13065477697463683111300f06035504031308416c6563204c6565301e170d3135303433303230323230315a170d3430303432333230323230315a306f310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d53616e204672616e636973636f310f300d060355040a1306547769746368310f300d060355040b13065477697463683111300f06035504031308416c6563204c656530819f300d06092a864886f70d010101050003818d0030818902818100d54f87980c3db56873ecc6a0e84bae7a8933b980c48ec69f0ecd6dc145420310c180dd6c41e21b4d7d878f17ad81ef341a9c0a526bb28240bdbc06ff4388e273b1c687ff3e7ecfd65921fce5e0f31bebec36f6977427153a994594ff051ba7a181206d90a56a6255cf344326a27c54e534ed6a55910c3aac3166b5e7b064eb9b0203010001300d06092a864886f70d0101050500038181001722cf86e5ca76831d1890a26d1fb40ce7cf3dcb392755c31587671a3511683c5e1359561dc4bd06a513f3a49e44820a0753c26542cf854295fd72a4c98d20f91a6985a7a8b6c022615a2200a99f37be65cf5910af32eb8ab3c1d79fd9bb0409c4420b2108e097c70532a71c19c7d41d9b1fa994e5e910fbd28aafb3c1037f73"
            )
        )
        private const val ORIGINAL_PACKAGE_NAME = "tv.twitch.android.app"

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
                    openUrl(
                        context = activity,
                        url = "https://www.twitch.tv/subs/${purchaseEntity.channelDisplayName}"
                    )
                }
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

        @JvmStatic
        fun hookPackageName(packageName: String?): String? {
            if (packageName.isNullOrBlank()) {
                return packageName
            }

            return if (packageName == get().context.packageName) {
                ORIGINAL_PACKAGE_NAME
            } else {
                packageName
            }
        }

        @JvmStatic
        fun hookRealPackageName(packageName: String?): String? {
            if (packageName.isNullOrBlank()) {
                return packageName
            }

            return if (packageName == ORIGINAL_PACKAGE_NAME) {
                get().context.packageName
            } else {
                packageName
            }
        }

        @JvmStatic
        fun hookPackageSignature(signArr: Array<Signature>?): Array<Signature>? {
            if (signArr.isNullOrEmpty()) {
                return signArr
            }

            val signature = signArr[0]
            if (signature == MOD_SIGNATURE[0]) {
                return TWITCH_SIGNATURE
            }
            return signArr
        }

        @JvmStatic
        fun maybeForceExoPlayerForVods(var3: TwitchPlayerProvider) {
            if (Flag.FORCE_EXOPLAYER_FOR_VODS.asBoolean()) {
                var3.useFallbackPlayer()
            }
        }

        @JvmStatic
        fun hookVodPlayerMediaClock(params: PlaybackParameters): PlaybackParameters {
            return if (ClassCompat.isOnStackTrace("tv.twitch.android.shared.player.presenters.VodPlayerPresenter") ||
                ClassCompat.isOnStackTrace("tv.twitch.android.shared.ads.AdsVodPlayerPresenter")
            ) {
                params.apply {
                    speed = Flag.EXOPLAYER_VOD_SPEED.asIntRange().getCurrentValue() / 100f
                }
            } else {
                params
            }
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
        LoggerImpl.debug("PurpleTV:${BuildConfigUtil.buildConfig.number}")
    }
}