package tv.orange.core

import tv.orange.core.di.component.CoreComponent
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import javax.inject.Inject

class Hook @Inject constructor() {
    companion object {
        private val INSTANCE: Hook by lazy {
            val provider = Core.get().provideComponent(CoreComponent::class)
            val hook = provider.get().coreHook

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }

        @JvmStatic
        fun inDevMode(): Boolean {
            return Flag.DEV_MODE.valueBoolean()
        }
    }
}