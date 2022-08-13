package tv.orange.features.settings.bridge

import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.compat.ClassCompat.getPrivateField
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

    private fun newInstance(subcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl): Provider<AndroidInjector.Factory<*>> {
        return Provider<AndroidInjector.Factory<*>> {
            OrangeSettingsFragmentSubcomponentFactory(
                subcomponentImpl.getPrivateField("provideFragmentActivityProvider"),
                subcomponentImpl.getPrivateField("provideMenuAdapterBinderProvider"),
                subcomponentImpl.getPrivateField("provideSettingsTrackerProvider")
            ) {
                OrangeSettings.get().getOrangeSettingsController(
                    subcomponentImpl.getPrivateField<Provider<FragmentActivity>>(
                        "provideFragmentActivityProvider"
                    ).get()
                )
            }
        }
    }
}