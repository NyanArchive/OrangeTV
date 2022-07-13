package tv.orange.features.settings.bridge

import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.compat.ClassCompat
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import javax.inject.Inject
import javax.inject.Provider


class OrangeSettingsDaggerFactory @Inject constructor() {
    fun injectSubcomponentSettingsProvider(
        mapBuilder: MapBuilder<Class<*>, Provider<AndroidInjector.Factory<*>>>,
        settingsActivitySubcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl
    ) {
        mapBuilder.put(
            OrangeSettingsFragment::class.java,
            newInstance(settingsActivitySubcomponentImpl)
        )
    }

    private fun newInstance(settingsActivitySubcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl): Provider<AndroidInjector.Factory<*>> {
        return Provider<AndroidInjector.Factory<*>> {
            OrangeSettingsFragmentSubcomponentFactory(
                ClassCompat.getProvider(
                    settingsActivitySubcomponentImpl,
                    "provideFragmentActivityProvider"
                ),
                ClassCompat.getProvider(
                    settingsActivitySubcomponentImpl,
                    "provideMenuAdapterBinderProvider"
                ),
                ClassCompat.getProvider(
                    settingsActivitySubcomponentImpl,
                    "provideSettingsTrackerProvider"
                ),
                { OrangeSettings.get().controller }
            )
        }
    }
}