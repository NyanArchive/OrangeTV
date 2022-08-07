package tv.orange.features.settings.component

import android.content.Context
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Internal
import tv.orange.core.models.flag.variants.PlayerImpl
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
        return Flag.values().mapNotNull {
            when (it.value) {
                is Internal.BooleanValue -> FlagToggleMenuModelExt(it)
                is Internal.ListValue<*> -> DropDownMenuModelExt<PlayerImpl>(it, context)
                else -> null
            }
        }
    }
}