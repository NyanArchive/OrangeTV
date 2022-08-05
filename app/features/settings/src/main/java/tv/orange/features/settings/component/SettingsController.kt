package tv.orange.features.settings.component

import android.content.Context
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.models.Flag
import tv.orange.core.models.variants.DeletedMessages
import tv.orange.core.models.variants.PlayerImpl
import tv.orange.features.settings.bridge.model.DropDownMenuModelExt
import tv.orange.features.settings.bridge.model.FlagToggleMenuModelExt
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

    fun getMainSettingModels(context: Context): Collection<MenuModel> {
        return listOf(
            FlagToggleMenuModelExt(Flag.DEV_MODE),
            FlagToggleMenuModelExt(Flag.BTTV_EMOTES),
            FlagToggleMenuModelExt(Flag.FFZ_EMOTES),
            FlagToggleMenuModelExt(Flag.STV_EMOTES),
            FlagToggleMenuModelExt(Flag.FFZ_BADGES),
            FlagToggleMenuModelExt(Flag.STV_BADGES),
            FlagToggleMenuModelExt(Flag.CHA_BADGES),
            FlagToggleMenuModelExt(Flag.CHE_BADGES),
            FlagToggleMenuModelExt(Flag.STV_AVATARS),
            FlagToggleMenuModelExt(Flag.CHAT_HISTORY),
            FlagToggleMenuModelExt(Flag.CHAT_TIMESTAMPS),
            FlagToggleMenuModelExt(Flag.DISABLE_STICKY_HEADERS_EP),
            FlagToggleMenuModelExt(Flag.HIDE_BITS_BUTTON),
            DropDownMenuModelExt<PlayerImpl>(Flag.PLAYER_IMPL, context),
            DropDownMenuModelExt<DeletedMessages>(Flag.DELETED_MESSAGES, context),
        )
    }
}