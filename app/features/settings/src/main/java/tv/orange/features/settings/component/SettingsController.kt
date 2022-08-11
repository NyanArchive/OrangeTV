package tv.orange.features.settings.component

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asString
import tv.orange.core.models.flag.Internal
import tv.orange.core.models.flag.variants.PlayerImpl
import tv.orange.features.settings.bridge.model.DropDownMenuModelExt
import tv.orange.features.settings.bridge.model.FlagToggleMenuModelExt
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel

class SettingsController(val activity: FragmentActivity) : SettingsPreferencesController {
    override fun updatePreferenceBooleanState(toggleMenuModel: ToggleMenuModel, state: Boolean) {
        val eventName = toggleMenuModel.eventName
        if (eventName.isNullOrBlank()) {
            Logger.debug("Skip: blank eventName")
            return
        }

        val flag = Flag.findByKey(eventName) ?: run {
            Logger.error("Flag not found for key: $eventName")
            return
        }

        PreferenceManager.get().writeBoolean(flag, state)
        checkIfNeedRestart(flag)
    }

    private fun checkIfNeedRestart(flag: Flag) {
        if (!RESTART_FLAGS.contains(flag)) {
            return
        }

        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(ResourceManager.get().getString("orange_restart_app_title"))
                setPositiveButton(
                    ResourceManager.get().getString("orange_restart_app")
                ) { _, _ ->
                    Core.restart(activity)
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
            when (it.value) {
                is Internal.BooleanValue -> FlagToggleMenuModelExt(it)
                is Internal.ListValue<*> -> DropDownMenuModelExt<PlayerImpl>(it, this)
                else -> null
            }
        }
    }

    fun onDropDownMenuItemSelection(flag: Flag, variant: Internal.Variant) {
        if (flag.asString() == variant.toString()) {
            Logger.debug("Ignore: $flag")
            return
        }

        PreferenceManager.get().writeString(flag, variant.toString())
        checkIfNeedRestart(flag)
    }

    companion object {
        private val RESTART_FLAGS = setOf(Flag.BOTTOM_NAVBAR_POSITION, Flag.DEV_MODE)
    }
}