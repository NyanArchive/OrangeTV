package tv.orange.features.settings.bridge.settings

import androidx.fragment.app.FragmentActivity
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder

class OrangeThirdPartySettingsPresenter constructor(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    controller: OrangeSettingsController
) : BasedSettingsPresenter(
    activity,
    adapterBinder,
    settingsTracker,
    controller,
    OrangeSubMenu.ThirdParty
)