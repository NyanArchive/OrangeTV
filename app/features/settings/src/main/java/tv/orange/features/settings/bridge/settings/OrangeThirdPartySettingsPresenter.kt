package tv.orange.features.settings.bridge.settings

import androidx.fragment.app.FragmentActivity
import tv.orange.core.ResourceManager
import tv.orange.features.settings.bridge.model.OrangeSubMenuWrapper
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.settings.base.BaseSettingsPresenter
import tv.twitch.android.settings.base.SettingsNavigationController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import javax.inject.Inject

class OrangeThirdPartySettingsPresenter @Inject constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    val controller: OrangeSettingsController
) : BaseSettingsPresenter(activity, adapterBinder, settingsTracker) {
    override fun getNavController(): SettingsNavigationController {
        return controller
    }

    override fun getPrefController(): SettingsPreferencesController {
        return controller
    }

    override fun getToolbarTitle(): String {
        return ResourceManager.get().getString(resName = "orange_settings_menu_third_party")
    }

    override fun updateSettingModels() {
        settingModels.clear()
        settingModels.addAll(OrangeSubMenuWrapper.ThirdParty.convertToMenuModels(controller))
    }
}