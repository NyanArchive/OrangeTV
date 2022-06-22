package tv.orange.features.core

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.features.core.di.CoreFeatureComponent
import tv.orange.features.core.di.CoreFeatureScope
import tv.orange.features.core.di.DaggerCoreFeatureComponent

@CoreFeatureScope
class CoreFeature constructor(val component: CoreFeatureComponent) {
    companion object {
        private val INSTANCE by lazy {
            val component = DaggerCoreFeatureComponent.builder()
                .coreComponent(Core.get().coreComponent)
                .build()

            val coreFeature = CoreFeature(component)
            Logger.debug("created: $component, $coreFeature")
            return@lazy coreFeature
        }

        @JvmStatic
        fun get(): CoreFeature {
            return INSTANCE
        }
    }
}