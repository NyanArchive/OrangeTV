package tv.orange.features.settings.bridge.slider

import android.content.Context
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.twitch.android.core.adapters.RecyclerAdapterItem
import tv.twitch.android.core.mvp.lifecycle.VisibilityProvider
import tv.twitch.android.shared.ui.menus.SettingActionListener
import tv.twitch.android.shared.ui.menus.core.MenuModel

class SliderModel(
    val flag: Flag,
    val listener: SliderListener
) : MenuModel.SingleItemModel(
    ResourceManager.get().getString(flag.titleResName!!),
    flag.summaryResName?.let { id ->
        ResourceManager.get().getString(resName = id)
    },
    null,
    null,
    null
) {

    interface SliderListener {
        fun onSliderValueChanged(flag: Flag, value: Int)
    }

    override fun toRecyclerAdapterItem(
        p0: Context,
        p1: SettingActionListener?,
        p2: VisibilityProvider?
    ): RecyclerAdapterItem {
        return SliderRecyclerItem(p0, this)
    }
}