package tv.orange.features.settings.bridge.model

import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.Internal
import tv.orange.features.settings.component.SettingsController
import tv.twitch.android.core.adapters.TwitchArrayAdapter
import tv.twitch.android.core.adapters.TwitchArrayAdapterModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel.DropDownMenuItemSelection

class DropDownMenuModelExt<T : Internal.Variant>(
    val flag: Flag,
    controller: SettingsController,
    private val raw: Boolean = false
) :
    DropDownMenuModel<TwitchArrayAdapterModel>(
        TwitchArrayAdapter(
            controller.activity,
            flag.asVariant<T>().getVariants().map { variant ->
                TwitchArrayAdapterModel {
                    if (raw) {
                        variant.toString()
                    } else {
                        ResourceManager.get().getString("orange_${flag.prefKey}_$variant")
                    }
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
            controller.onDropDownMenuItemSelection(
                flag,
                flag.asVariant<T>().getVariants()[position]
            )
        }
    ) {
    override fun getSelectedOption(): Int {
        return flag.asVariant<T>().getVariants().indexOf(flag.asVariant())
    }
}
