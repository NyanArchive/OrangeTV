package tv.orange.features.settings

import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.settings.bridge.OrangeSettingsDaggerFactory
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment
import tv.orange.features.settings.component.SettingsController
import tv.orange.features.settings.di.component.DaggerSettingsComponent
import tv.orange.features.settings.di.scope.SettingsScope
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import javax.inject.Inject
import javax.inject.Provider

@SettingsScope
class OrangeSettings @Inject constructor(
    val daggerFactory: OrangeSettingsDaggerFactory,
    val controller: SettingsController
) {
    fun inject(
        builder: MapBuilder<Class<*>, Provider<AndroidInjector.Factory<*>>>,
        component: DaggerAppComponent.SettingsActivitySubcomponentImpl
    ) {
        daggerFactory.injectSubcomponentSettingsProvider(builder, component)
    }

    fun createSettingsFragment(): Fragment {
        return OrangeSettingsFragment()
    }

    companion object {
        private val INSTANCE by lazy {
            val instance = DaggerSettingsComponent.factory()
                .create(Core.getProvider(CoreComponent::class).get()).orangeSettings

            Logger.debug("Provide new instance: $instance")
            return@lazy instance
        }

        @JvmStatic
        fun get(): OrangeSettings {
            return INSTANCE
        }
    }
}