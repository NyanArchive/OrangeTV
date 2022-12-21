package tv.orange.features.settings.bridge.settings

import androidx.fragment.app.FragmentActivity
import tv.orange.core.ResourceManager
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.models.settings.SettingsDestination
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.subscription.SubMenuModel

class OrangeChatSettingsPresenter constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    controller: OrangeSettingsController,
) : BasedSettingsPresenter(
    activity,
    adapterBinder,
    settingsTracker,
    controller,
    OrangeSubMenu.Chat
) {
    override fun updateSettingModels() {
        super.updateSettingModels()
        addHighlighterSubmenu()
    }

    private fun addHighlighterSubmenu() {
        settingModels.add(
            SubMenuModel(
                ResourceManager.get().getString("orange_settings_menu_highlighter"),
                null,
                null,
                SettingsDestination.OrangeHighlighter,
                true
            ) as MenuModel
        )
        settingModels.add(
            SubMenuModel(
                ResourceManager.get().getString("orange_settings_menu_blacklist"),
                null,
                null,
                SettingsDestination.OrangeBlacklist,
                true
            ) as MenuModel
        )
    }
}