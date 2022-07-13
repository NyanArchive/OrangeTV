package tv.orange.features.settings.bridge

import androidx.fragment.app.FragmentActivity
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment
import tv.orange.features.settings.component.SettingsController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import javax.inject.Provider


class OrangeSettingsFragmentSubcomponentFactory(
    val activityProvider: Provider<FragmentActivity>,
    val adapterBinderProvider: Provider<MenuAdapterBinder>,
    val settingsTrackerProvider: Provider<SettingsTracker>,
    val settingsControllerProvider: Provider<SettingsController>
) : OrangeSettingsFragmentSubcomponentBinding.Factory {

    private fun newInstance(fragmentActivity: FragmentActivity, adapterBinder: MenuAdapterBinder, settingsTracker: SettingsTracker, controller: SettingsController): OrangeSettingsFragmentSubcomponentBindingImpl {
        return OrangeSettingsFragmentSubcomponentBindingImpl(fragmentActivity, adapterBinder, settingsTracker, controller)
    }

    override fun create(systemSettingsFragment: OrangeSettingsFragment): OrangeSettingsFragmentSubcomponentBinding {
        return newInstance(activityProvider.get(), adapterBinderProvider.get(), settingsTrackerProvider.get(), settingsControllerProvider.get())
    }
}