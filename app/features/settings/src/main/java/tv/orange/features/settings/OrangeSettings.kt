package tv.orange.features.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import tv.orange.features.settings.bridge.OrangeSettingsDaggerFactory
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment
import tv.orange.features.settings.component.SettingsController
import tv.orange.features.settings.di.scope.SettingsScope
import tv.orange.models.abc.Feature
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import javax.inject.Inject
import javax.inject.Provider

@SettingsScope
class OrangeSettings @Inject constructor(
    val daggerFactory: OrangeSettingsDaggerFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(OrangeSettings::class.java)

        @JvmStatic
        fun getOrangeSettingsString(): String {
            return ResourceManager.get().getString("orange_settings")
        }
    }

    fun getOrangeSettingsController(activity: FragmentActivity): SettingsController {
        return SettingsController(activity)
    }

    fun inject(
        builder: MapBuilder<Class<*>, Provider<AndroidInjector.Factory<*>>>,
        component: DaggerAppComponent.SettingsActivitySubcomponentImpl
    ) {
        daggerFactory.injectSubcomponentSettingsProvider(builder, component)
    }

    fun createSettingsFragment(): Fragment {
        return OrangeSettingsFragment()
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}