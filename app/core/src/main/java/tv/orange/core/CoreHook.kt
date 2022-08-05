package tv.orange.core

import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.core.models.Flag.Companion.variant
import tv.orange.core.models.variants.PlayerImpl
import tv.twitch.android.models.player.PlayerImplementation
import javax.inject.Inject


class CoreHook @Inject constructor() {
    companion object {
        @JvmStatic
        fun inDevMode(): Boolean {
            return Flag.DEV_MODE.valueBoolean()
        }

        @JvmStatic
        fun hookPlayerImplementation(default: PlayerImplementation): PlayerImplementation {
            return when (Flag.PLAYER_IMPL.variant<PlayerImpl>()) {
                PlayerImpl.Default -> default
                PlayerImpl.Core -> PlayerImplementation.Core
                PlayerImpl.Exo -> PlayerImplementation.Exo2
            }
        }
    }
}