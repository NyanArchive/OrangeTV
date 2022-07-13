package tv.orange.features.settings.bridge.model

import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel

open class ToggleMenuModel(
    primaryText: String,
    secondaryText: String? = null,
    auxiliaryText: String? = null,
    state: Boolean,
    eventName: String
) : ToggleMenuModel(
    primaryText,
    secondaryText,
    auxiliaryText,
    state,
    true,
    null,
    eventName,
    false,
    null,
    null,
    null,
    SettingsPreferencesController.SettingsPreference.AdTracking,
    null
)