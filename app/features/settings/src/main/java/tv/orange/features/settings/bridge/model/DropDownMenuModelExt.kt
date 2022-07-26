package tv.orange.features.settings.bridge.model

import android.content.Context
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.variant
import tv.twitch.android.core.adapters.TwitchArrayAdapter
import tv.twitch.android.core.adapters.TwitchArrayAdapterModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel.DropDownMenuItemSelection

class DropDownMenuModelExt<T : Flag.Variant>(flag: Flag, context: Context) :
    DropDownMenuModel<TwitchArrayAdapterModel>(
        TwitchArrayAdapter(
            context,
            flag.variant<T>().getVariants().map { variant ->
                TwitchArrayAdapterModel {
                    ResourceManager.getString("orange_${flag.prefKey}_$variant")
                }
            },
            ResourceManager.getId("twitch_spinner_dropdown_item", "layout")
        ),
        flag.variant<T>().getVariants().indexOf(flag.variant()),
        ResourceManager.getString(flag.titleRes),
        ResourceManager.getString(flag.summaryRes),
        null,
        null,
        DropDownMenuItemSelection { _, position ->
            PreferenceManager.get()
                .writeString(flag, flag.variant<T>().getVariants()[position].toString())
        }
    )
