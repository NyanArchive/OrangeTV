package tv.orange.core

import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.PlayerImpl
import tv.orange.models.abc.Feature
import tv.twitch.android.models.player.PlayerImplementation
import javax.inject.Inject

class CoreHook @Inject constructor() : Feature {
    companion object {
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
    }

    override fun onCreateFeature() {}
}