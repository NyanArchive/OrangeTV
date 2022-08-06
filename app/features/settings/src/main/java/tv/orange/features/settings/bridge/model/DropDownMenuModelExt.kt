package tv.orange.features.settings.bridge.model

import android.content.Context
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.Internal
import tv.twitch.android.core.adapters.TwitchArrayAdapter
import tv.twitch.android.core.adapters.TwitchArrayAdapterModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel.DropDownMenuItemSelection

class DropDownMenuModelExt<T : Internal.Variant>(flag: Flag, context: Context) :
    DropDownMenuModel<TwitchArrayAdapterModel>(
        TwitchArrayAdapter(
            context,
            flag.asVariant<T>().getVariants().map { variant ->
                TwitchArrayAdapterModel {
                    ResourceManager.get().getString("orange_${flag.prefKey}_$variant")
                }
            },
            ResourceManager.get().getLayoutId("twitch_spinner_dropdown_item")
        ),
        flag.asVariant<T>().getVariants().indexOf(flag.asVariant()),
        flag.titleRes?.let { id ->
            ResourceManager.get().getString(id)
        },
        flag.summaryRes?.let { id ->
            ResourceManager.get().getString(id)
        },
        null,
        null,
        DropDownMenuItemSelection { _, position ->
            PreferenceManager.get().writeString(
                flag = flag,
                value = flag.asVariant<T>().getVariants()[position].toString()
            )
        }
    )
