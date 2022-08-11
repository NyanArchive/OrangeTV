package tv.orange.features.settings.bridge.fragment

import androidx.fragment.app.FragmentActivity
import tv.orange.core.Logger
import tv.orange.core.ResourceManager
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.settings.component.SettingsController
import tv.twitch.android.settings.base.BaseSettingsPresenter
import tv.twitch.android.settings.base.SettingsNavigationController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import javax.inject.Inject

class OrangeSettingsPresenter @Inject constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    val controller: SettingsController
) : BaseSettingsPresenter(activity, adapterBinder, settingsTracker) {
    override fun getNavController(): SettingsNavigationController {
        return SettingsNavigationController { p0, p1 -> Logger.debug("$p0 -> $p1") }
    }

    override fun getPrefController(): SettingsPreferencesController {
        return OrangeSettings.get().getOrangeSettingsController(activity)
    }

    override fun getToolbarTitle(): String {
        return ResourceManager.get().getString("orange_settings_title")
    }

    override fun updateSettingModels() {
        settingModels.clear();
        settingModels.addAll(controller.getMainSettingModels())
    }
}