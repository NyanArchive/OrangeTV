package tv.orange.features.settings.component

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asString
import tv.orange.core.models.flag.Internal
import tv.orange.core.models.flag.variants.FontSize
import tv.orange.features.settings.bridge.model.DropDownMenuModelExt
import tv.orange.features.settings.bridge.model.FlagToggleMenuModelExt
import tv.orange.features.settings.bridge.slider.SliderModel
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel

class SettingsController(val activity: FragmentActivity) : SettingsPreferencesController,
    SliderModel.SliderListener {
    override fun updatePreferenceBooleanState(toggleMenuModel: ToggleMenuModel, state: Boolean) {
        val eventName = toggleMenuModel.eventName
        if (eventName.isNullOrBlank()) {
            return
        }

        val flag = Flag.findByKey(eventName) ?: run {
            Logger.error("Flag not found: $eventName")
            return
        }

        PreferenceManager.get().writeBoolean(flag = flag, value = state)
        checkIfNeedRestart(flag = flag)
    }

    private fun checkIfNeedRestart(flag: Flag) {
        if (!RESTART_FLAGS.contains(flag)) {
            return
        }

        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(ResourceManager.get().getString(resName = "orange_restart_app_title"))
                setPositiveButton(
                    ResourceManager.get().getString(resName = "orange_restart_app")
                ) { _, _ ->
                    Core.restart(activity = activity)
                }
                setNegativeButton(
                    android.R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }

            builder.create()
        }

        alertDialog.show()
    }

    fun getMainSettingModels(): Collection<MenuModel> {
        return Flag.values().mapNotNull {
            when (it.valueHolder) {
                is Internal.BooleanValue -> FlagToggleMenuModelExt(it)
                is Internal.ListValue<*> -> if (it == Flag.CHAT_FONT_SIZE) {
                    DropDownMenuModelExt<FontSize>(it, this, true)
                } else {
                    DropDownMenuModelExt(it, this)
                }
                is Internal.IntegerRangeValue -> SliderModel(it, this)
                else -> null
            }
        }
    }

    fun onDropDownMenuItemSelection(flag: Flag, variant: Internal.Variant) {
        if (flag.asString() == variant.toString()) {
            return
        }

        PreferenceManager.get().writeString(flag = flag, value = variant.toString())
        checkIfNeedRestart(flag)
    }

    companion object {
        private val RESTART_FLAGS = setOf(Flag.BOTTOM_NAVBAR_POSITION, Flag.DEV_MODE)
    }

    override fun onSliderValueChanged(flag: Flag, value: Int) {
        if (flag.asInt() == value) {
            return
        }

        PreferenceManager.get().writeInt(flag = flag, value = value)
        checkIfNeedRestart(flag)
    }
}