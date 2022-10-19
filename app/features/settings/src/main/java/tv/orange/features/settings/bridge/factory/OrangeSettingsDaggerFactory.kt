package tv.orange.features.settings.bridge.factory

import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjector
import dagger.internal.MapBuilder
import tv.orange.core.compat.ClassCompat.getPrivateField
import tv.orange.features.highlighter.Highlighter
import tv.orange.features.settings.bridge.DaggerMapper
import tv.orange.features.settings.bridge.IFragment
import tv.orange.features.settings.bridge.settings.OrangeSettingsFragment
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.app.consumer.dagger.AppComponent
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import tv.twitch.android.routing.routers.IFragmentRouter
import tv.twitch.android.settings.base.BaseSettingsPresenter
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import javax.inject.Inject
import javax.inject.Provider

class OrangeSettingsDaggerFactory @Inject constructor(
    val highlighter: Highlighter
) {
    fun injectSubcomponentSettingsProvider(
        mapBuilder: MapBuilder<Class<*>, Provider<AndroidInjector.Factory<*>>>,
        settingsActivitySubcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl,
        appComponent: AppComponent
    ) {
        val orangeSettingsControllerProvider = provideOrangeSettingsController(
            settingsActivitySubcomponentImpl,
            appComponent
        )
        DaggerMapper.values().associate {
            it.fragment to buildInjector<OrangeSettingsFragment>(
                settingsActivitySubcomponentImpl,
                orangeSettingsControllerProvider,
                it.presenter
            )
        }.forEach { map ->
            mapBuilder.put(map.key, map.value)
        }
    }

    private fun provideOrangeSettingsController(
        settingsActivitySubcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl,
        appComponent: AppComponent
    ): Provider<OrangeSettingsController> {
        return Provider {
            OrangeSettingsController(
                activity = settingsActivitySubcomponentImpl.getPrivateField<Provider<FragmentActivity>>(
                    "provideFragmentActivityProvider"
                ).get(),
                fragmentRouter = appComponent.getPrivateField<Provider<IFragmentRouter>>(
                    "provideFragmentRouterProvider"
                ).get(),
                highlighter = highlighter
            )
        }
    }

    companion object {
        fun <T : IFragment> buildInjector(
            subcomponentImpl: DaggerAppComponent.SettingsActivitySubcomponentImpl,
            orangeSettingsControllerProvider: Provider<OrangeSettingsController>,
            clazz: Class<out BaseSettingsPresenter>
        ): Provider<AndroidInjector.Factory<*>> {
            return Provider<AndroidInjector.Factory<*>> {
                object : AbstractOrangeSettingsFragmentSubcomponentFactory<T>(
                    subcomponentImpl.getPrivateField("provideFragmentActivityProvider"),
                    subcomponentImpl.getPrivateField("provideMenuAdapterBinderProvider"),
                    subcomponentImpl.getPrivateField("provideSettingsTrackerProvider"),
                    orangeSettingsControllerProvider
                ) {
                    override fun providePresenter(
                        fragmentActivity: FragmentActivity,
                        adapterBinder: MenuAdapterBinder,
                        settingsTracker: SettingsTracker,
                        orangeSettingsController: OrangeSettingsController
                    ): BaseSettingsPresenter {
                        return clazz.constructors.first().newInstance(
                            fragmentActivity,
                            adapterBinder,
                            settingsTracker,
                            orangeSettingsController
                        ) as BaseSettingsPresenter
                    }
                }
            }
        }
    }
}