package tv.orange.features.settings.component

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asString
import tv.orange.core.models.flag.Internal
import tv.orange.features.settings.bridge.model.OrangeSubMenuWrapper
import tv.orange.features.settings.bridge.settings.*
import tv.orange.features.settings.bridge.slider.SliderModel
import tv.twitch.android.models.settings.SettingsDestination
import tv.twitch.android.routing.routers.IFragmentRouter
import tv.twitch.android.settings.base.SettingsNavigationController
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.subscription.SubMenuModel
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel

class OrangeSettingsController(
    val activity: FragmentActivity,
    val fragmentRouter: IFragmentRouter
) : SettingsPreferencesController,
    SliderModel.SliderListener, SettingsNavigationController {
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
        return OrangeSubMenuWrapper.values().filter { it.items.isNotEmpty() }
            .map {
                SubMenuModel(ResourceManager.get().getString(it.title), it.desc?.let { desc ->
                    ResourceManager.get().getString(desc)
                }, null, it.destination, true) as MenuModel
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
        checkIfNeedRestart(flag = flag)
    }

    override fun navigateToSettingFragment(settingsDestination: SettingsDestination, p1: Bundle?) {
        when (settingsDestination) {
            SettingsDestination.OrangeThirdParty -> OrangeThirdPartySettingsFragment()
            SettingsDestination.OrangeChat -> OrangeChatSettingsFragment()
            SettingsDestination.OrangePlayer -> OrangePlayerSettingsFragment()
            SettingsDestination.OrangeView -> OrangeViewSettingsFragment()
            SettingsDestination.OrangeDev -> OrangeDevSettingsFragment()
            else -> null
        }?.let { fragment ->
            fragmentRouter.addOrRecreateFragment(
                activity,
                fragment,
                settingsDestination.toString(),
                Bundle()
            )
        }
    }
}