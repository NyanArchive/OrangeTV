package tv.orange.features.settings.bridge.model

import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.core.Variant
import tv.orange.core.models.flag.core.Variant.Companion.getVariants
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.core.adapters.TwitchArrayAdapter
import tv.twitch.android.core.adapters.TwitchArrayAdapterModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel
import tv.twitch.android.shared.ui.menus.dropdown.DropDownMenuModel.DropDownMenuItemSelection

class DropDownMenuModelExt<T : Variant>(
    private val flag: Flag,
    controller: OrangeSettingsController,
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
                        ResourcesManagerCore.get().getString(
                            resName = "orange_${flag.preferenceKey}_$variant"
                        )
                    }
                }
            },
            ResourcesManagerCore.get().getLayoutId(resName = "twitch_spinner_dropdown_item")
        ),
        flag.asVariant<T>().getVariants().indexOf(flag.asVariant()),
        flag.titleResName?.let { id ->
            ResourcesManagerCore.get().getString(resName = id)
        },
        flag.summaryResName?.let { id ->
            ResourcesManagerCore.get().getString(resName = id)
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
