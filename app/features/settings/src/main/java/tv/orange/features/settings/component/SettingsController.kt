package tv.orange.features.settings.component

import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.models.Flag
import tv.orange.features.settings.bridge.model.FlagToggleMenuModel
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel
import javax.inject.Inject

class SettingsController @Inject constructor() : SettingsPreferencesController {
    override fun updatePreferenceBooleanState(toggleMenuModel: ToggleMenuModel, state: Boolean) {
        val eventName = toggleMenuModel.eventName
        if (eventName.isNullOrBlank()) {
            Logger.debug("Skip: blank eventName")
            return
        }

        PreferenceManager.get().writeBoolean(eventName, state)
    }

    fun getMainSettingModels(): Collection<MenuModel> {
        return listOf(
            FlagToggleMenuModel(Flag.DEV_MODE),
            FlagToggleMenuModel(Flag.BTTV_EMOTES),
            FlagToggleMenuModel(Flag.FFZ_EMOTES),
            FlagToggleMenuModel(Flag.STV_EMOTES),
            FlagToggleMenuModel(Flag.FFZ_BADGES),
            FlagToggleMenuModel(Flag.STV_BADGES),
            FlagToggleMenuModel(Flag.CHA_BADGES),
            FlagToggleMenuModel(Flag.CHE_BADGES)
        )
    }
}