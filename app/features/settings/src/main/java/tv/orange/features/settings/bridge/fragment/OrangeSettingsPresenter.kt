package tv.orange.features.settings.bridge.fragment

import androidx.fragment.app.FragmentActivity
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.settings.bridge.model.ToggleMenuModel
import tv.twitch.android.settings.base.BaseSettingsPresenter
import tv.twitch.android.settings.base.SettingsNavigationController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import javax.inject.Inject


class OrangeSettingsPresenter @Inject constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker
) : BaseSettingsPresenter(activity, adapterBinder, settingsTracker) {
    override fun getNavController(): SettingsNavigationController {
        return SettingsNavigationController { p0, p1 -> Logger.debug("$p0 -> $p1") }
    }

    override fun getPrefController(): SettingsPreferencesController {
        Logger.debug("called")

        return OrangeSettings.get().controller
    }

    override fun getToolbarTitle(): String {
        Logger.debug("called")

        return "Hello world!"
    }

    override fun updateSettingModels() {
        Logger.debug("called")
        settingModels.clear();
        settingModels.add(
            ToggleMenuModel(
                primaryText = "Dev mode",
                state = PreferenceManager.devMode,
                eventName = "devMode"
            )
        )
    }
}