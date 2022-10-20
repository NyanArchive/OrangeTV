package tv.orange.features.settings

import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import tv.orange.features.settings.bridge.factory.OrangeSettingsDaggerFactory
import tv.orange.features.settings.bridge.settings.OrangeSettingsFragment
import tv.orange.models.abc.Feature
import tv.twitch.android.app.consumer.dagger.AppComponent
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import javax.inject.Inject
import javax.inject.Provider

class OrangeSettings @Inject constructor(
    val daggerFactory: OrangeSettingsDaggerFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(OrangeSettings::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(OrangeSettings::class.java)
        }

        @JvmStatic
        fun getOrangeSettingsString(): String {
            return ResourceManager.get().getString(resName = "orange_settings")
        }
    }

    fun inject(
        builder: MapBuilder<Class<*>, Provider<AndroidInjector.Factory<*>>>,
        settingsActivitySubcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl,
        appComponent: AppComponent
    ) {
        daggerFactory.injectSubcomponentSettingsProvider(
            mapBuilder = builder,
            settingsActivitySubcomponentImpl = settingsActivitySubcomponentImpl,
            appComponent = appComponent
        )
    }

    fun createSettingsFragment(): Fragment {
        return OrangeSettingsFragment()
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}