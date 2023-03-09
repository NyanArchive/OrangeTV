package tv.orange.core

import android.app.Activity
import android.content.pm.Signature
import com.google.android.exoplayer2.SimpleExoPlayer
import io.reactivex.Single
import tv.orange.core.compat.ClassCompat
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.PinnedMessageStrategy
import tv.orange.core.models.flag.variants.PlayerImpl
import tv.orange.models.abc.Feature
import tv.twitch.android.models.player.PlayerImplementation
import tv.twitch.android.shared.player.TwitchPlayerProvider
import tv.twitch.android.shared.subscriptions.db.SubscriptionPurchaseEntity
import tv.twitch.android.shared.subscriptions.purchasers.SubscriptionPurchaseResponse
import tv.twitch.android.util.CoreDateUtil
import javax.inject.Inject

class CoreHook @Inject constructor() : Feature {
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

        private const val MAX_PINNED_MESSAGE_MS = 30 * 1000

        @JvmStatic
        fun get() = Core.getFeature(CoreHook::class.java)

        @JvmStatic
        fun inDevMode(): Boolean {
            return Flag.DEV_MODE.asBoolean()
        }

        @JvmStatic
        val fastBread: Boolean
            get() = !Flag.DISABLE_FAST_BREAD.asBoolean()

        @JvmStatic
        fun hookPlayerImplementation(default: PlayerImplementation): PlayerImplementation {
            return when (Flag.PLAYER_IMPL.asVariant<PlayerImpl>()) {
                PlayerImpl.Default -> default
                PlayerImpl.Core -> PlayerImplementation.Core
                PlayerImpl.Exo -> PlayerImplementation.Exo2
            }
        }

        @JvmStatic
        fun maybeForceExoPlayerForVods(provider: TwitchPlayerProvider) {
            if (Flag.FORCE_EXOPLAYER_FOR_VODS.asBoolean()) {
                provider.useFallbackPlayer()
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
        fun hookRealPackageName(packageName: String?): String? {
            if (packageName.isNullOrBlank()) {
                return packageName
            }

            return if (packageName == ORIGINAL_PACKAGE_NAME) {
                Core.get().context.packageName
            } else {
                packageName
            }
        }

        @JvmStatic
        fun hookPackageName(packageName: String?): String? {
            if (packageName.isNullOrBlank()) {
                return packageName
            }

            return if (packageName == Core.get().context.packageName) {
                ORIGINAL_PACKAGE_NAME
            } else {
                packageName
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
                    Core.openUrl(
                        context = activity,
                        url = "https://www.twitch.tv/subs/${purchaseEntity.channelDisplayName}"
                    )
                }
            }
        }

        @JvmStatic
        fun hookUnpinnedMS(timeMessageUnpinnedMS: Long?): Long? {
            return when (Flag.PINNED_MESSAGE.asVariant<PinnedMessageStrategy>()) {
                PinnedMessageStrategy.Disabled,
                PinnedMessageStrategy.Default -> timeMessageUnpinnedMS
                PinnedMessageStrategy.SEC30 -> {
                    val calc = CoreDateUtil().currentTimeInMillis + MAX_PINNED_MESSAGE_MS
                    timeMessageUnpinnedMS?.let { ms ->
                        if (ms - calc - 1000 <= 0) {
                            ms
                        } else {
                            calc
                        }
                    } ?: calc
                }
            }
        }

        @JvmStatic
        fun isPinnedChatMessageEnabled(): Boolean {
            return Flag.PINNED_MESSAGE.asVariant<PinnedMessageStrategy>() != PinnedMessageStrategy.Disabled
        }

        @JvmStatic
        fun test(player: SimpleExoPlayer) {
        }
    }

    override fun onCreateFeature() {}
}