package tv.orange.features.settings

import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.features.settings.bridge.OrangeSettingsDaggerFactory
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment
import tv.orange.features.settings.component.SettingsController
import tv.orange.features.settings.di.scope.SettingsScope
import tv.orange.models.Feature
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import javax.inject.Inject
import javax.inject.Provider

@SettingsScope
class OrangeSettings @Inject constructor(
    val daggerFactory: OrangeSettingsDaggerFactory,
    val controller: SettingsController
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(OrangeSettings::class.java)
    }

    fun inject(
        builder: MapBuilder<Class<*>, Provider<AndroidInjector.Factory<*>>>,
        component: DaggerAppComponent.SettingsActivitySubcomponentImpl
    ) {
        Logger.debug("builder: $builder, component: $component")
        daggerFactory.injectSubcomponentSettingsProvider(builder, component)
    }

    fun createSettingsFragment(): Fragment {
        val fragment = OrangeSettingsFragment()
        Logger.debug("fragment: $fragment")
        return fragment
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}