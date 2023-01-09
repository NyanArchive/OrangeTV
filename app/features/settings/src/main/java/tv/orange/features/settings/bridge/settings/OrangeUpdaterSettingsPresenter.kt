package tv.orange.features.settings.bridge.settings

import android.text.format.Formatter
import androidx.fragment.app.FragmentActivity
import tv.orange.core.ResourcesManagerCore
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.component.OrangeSettingsController
import tv.orange.features.updater.Updater
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.infomenu.InfoMenuModel

class OrangeUpdaterSettingsPresenter constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    controller: OrangeSettingsController
) : BasedSettingsPresenter(
    activity,
    adapterBinder,
    settingsTracker,
    controller,
    OrangeSubMenu.OTA
) {

    override fun updateSettingModels() {
        super.updateSettingModels()
        settingModels.add(
            InfoMenuModel(
                ResourcesManagerCore.get().getString("orange_settings_check_update"),
                null,
                null, null, null, null
            ) {
                Updater.get().onCheckUpdateClicked(activity)
            } as MenuModel
        )
        settingModels.add(
            InfoMenuModel(
                ResourcesManagerCore.get().getString("orange_settings_clear_update_cache"),
                Formatter.formatFileSize(activity, Updater.get().calcCacheSize(activity)),
                null, null, null, null
            ) {
                Updater.get().onClearCacheClicked(activity)
            } as MenuModel
        )
    }
}