package tv.orange.features.settings.bridge.factory

import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjector
import tv.orange.features.settings.bridge.IFragment
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.settings.base.BaseSettingsPresenter
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import javax.inject.Provider

abstract class AbstractOrangeSettingsFragmentSubcomponentFactory<T : IFragment>(
    private val fragmentActivityProvider: Provider<FragmentActivity>,
    private val menuAdapterBinderProvider: Provider<MenuAdapterBinder>,
    private val settingsTrackerProvider: Provider<SettingsTracker>,
    private val orangeSettingsControllerProvider: Provider<OrangeSettingsController>
) : AndroidInjector.Factory<T> {

    abstract fun providePresenter(
        fragmentActivity: FragmentActivity,
        adapterBinder: MenuAdapterBinder,
        settingsTracker: SettingsTracker,
        orangeSettingsController: OrangeSettingsController
    ): BaseSettingsPresenter

    override fun create(fragment: T): AndroidInjector<T> {
        return AndroidInjector<T> {
            fragment.setPresenter(
                providePresenter(
                    fragmentActivityProvider.get(),
                    menuAdapterBinderProvider.get(),
                    settingsTrackerProvider.get(),
                    orangeSettingsControllerProvider.get()
                )
            )
        }
    }
}