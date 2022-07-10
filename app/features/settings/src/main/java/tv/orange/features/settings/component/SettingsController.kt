package tv.orange.features.settings.component

import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel
import javax.inject.Inject

class SettingsController @Inject constructor() : SettingsPreferencesController {
    override fun updatePreferenceBooleanState(toggleMenuModel: ToggleMenuModel, state: Boolean) {
        val eventName = toggleMenuModel.eventName
        if (eventName.isNullOrBlank()) {
            Logger.debug("Skip: blank eventName")
            return
        }

        when (eventName) {
            "devMode" -> toggleDevMode(state)
            else -> Logger.debug("unknown event: $eventName")
        }
    }

    private fun toggleDevMode(state: Boolean) {
        PreferenceManager.get().toggleDevMode(state)
    }
}