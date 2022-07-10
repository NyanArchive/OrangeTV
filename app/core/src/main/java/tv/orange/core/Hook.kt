package tv.orange.core

import tv.orange.core.di.component.CoreComponent
import javax.inject.Inject

class Hook @Inject constructor() {
    companion object {
        private val INSTANCE: Hook by lazy {
            val hook = Core.getInjector().provideComponent(CoreComponent::class).hook

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }

        @JvmStatic
        fun inDevMode(): Boolean {
            return PreferenceManager.devMode
        }
    }
}