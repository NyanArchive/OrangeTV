package tv.orange.core

import tv.orange.core.di.component.CoreComponent
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.core.models.Flag.Companion.valueString
import tv.twitch.android.models.player.PlayerImplementation
import javax.inject.Inject

class Hook @Inject constructor() {
    companion object {
        private val INSTANCE: Hook by lazy {
            val instance = Core.getProvider(CoreComponent::class).get().coreHook

            Logger.debug("Provide new instance: $instance")
            return@lazy instance
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }

        @JvmStatic
        fun inDevMode(): Boolean {
            return Flag.DEV_MODE.valueBoolean()
        }

        @JvmStatic
        fun hookPlayerImplementation(default: PlayerImplementation): PlayerImplementation {
            return when (Flag.PLAYER_IMPL.valueString()) {
                "default" -> default
                "core" -> PlayerImplementation.Core
                "exo" -> PlayerImplementation.Exo2
                else -> default
            }
        }
    }
}