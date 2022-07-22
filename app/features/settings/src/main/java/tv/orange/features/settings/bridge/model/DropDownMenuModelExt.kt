package tv.orange.features.settings.bridge.model

import android.content.Context
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueList
import tv.orange.core.models.Flag.Companion.valueString
import tv.twitch.android.core.adapters.TwitchArrayAdapter
import tv.twitch.android.core.adapters.TwitchArrayAdapterModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel.DropDownMenuItemSelection

class DropDownMenuModelExt(flag: Flag, context: Context) :
    DropDownMenuModel<TwitchArrayAdapterModel>(
        TwitchArrayAdapter(
            context,
            flag.valueList().map { variant ->
                TwitchArrayAdapterModel {
                    ResourceManager.getString("${flag.prefKey}_$variant")
                }
            },
            ResourceManager.getId("twitch_spinner_dropdown_item", "layout")
        ),
        flag.valueList().indexOf(flag.valueString()),
        ResourceManager.getString(flag.titleRes),
        ResourceManager.getString(flag.summaryRes),
        null,
        null,
        DropDownMenuItemSelection { _, position ->
            PreferenceManager.get().writeString(flag, flag.valueList()[position])
        }
    )
