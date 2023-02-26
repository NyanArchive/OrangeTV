package tv.orange.features.settings.bridge.settings

import androidx.fragment.app.FragmentActivity
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder

class OrangeDevSettingsPresenter constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    controller: OrangeSettingsController
) : BasedSettingsPresenter(
    activity,
    adapterBinder,
    settingsTracker,
    controller,
    OrangeSubMenu.Dev
) {
    override fun updateSettingModels() {
        super.updateSettingModels()
        if (Flag.DEV_MODE.asBoolean()) {
            settingModels.addAll(
                convertToMenuModels(
                    items = listOf(Flag.FORCE_DISABLE_SENTRY),
                    controller = prefController as OrangeSettingsController
                )
            )
        }
    }
}