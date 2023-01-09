package tv.orange.features.settings.component

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asString
import tv.orange.core.models.flag.Flag.Companion.setValue
import tv.orange.core.models.flag.core.Variant
import tv.orange.features.blacklist.Blacklist
import tv.orange.features.highlighter.Highlighter
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.bridge.settings.*
import tv.orange.features.settings.bridge.slider.SliderModel
import tv.twitch.android.models.settings.SettingsDestination
import tv.twitch.android.routing.routers.IFragmentRouter
import tv.twitch.android.settings.base.SettingsNavigationController
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuModel
import tv.twitch.android.shared.ui.menus.subscription.SubMenuModel
import tv.twitch.android.shared.ui.menus.togglemenu.ToggleMenuModel
import javax.inject.Inject

class OrangeSettingsController @Inject constructor(
    val activity: FragmentActivity,
    val fragmentRouter: IFragmentRouter,
    val highlighter: Highlighter,
    val blacklist: Blacklist
) : SettingsPreferencesController, SliderModel.SliderListener, SettingsNavigationController {
    override fun updatePreferenceBooleanState(toggleMenuModel: ToggleMenuModel, state: Boolean) {
        val eventName = toggleMenuModel.eventName
        if (eventName.isNullOrBlank()) {
            return
        }

        val flag = Flag.findByKey(eventName) ?: run {
            LoggerImpl.error("Flag not found: $eventName")
            return
        }

        flag.setValue(state)
        checkIfNeedRestart(flag = flag)
    }

    private fun checkIfNeedRestart(flag: Flag) {
        if (!RESTART_FLAGS.contains(flag)) {
            return
        }

        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(ResourcesManagerCore.get().getString(resName = "orange_restart_app_title"))
                setPositiveButton(
                    ResourcesManagerCore.get().getString(resName = "orange_restart_app")
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

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun getMainSettingModels(): Collection<MenuModel> {
        return OrangeSubMenu.values().filter { it.items.isNotEmpty() || it == OrangeSubMenu.Info }
            .map {
                SubMenuModel(ResourcesManagerCore.get().getString(it.title), it.desc?.let { desc ->
                    ResourcesManagerCore.get().getString(desc)
                }, null, it.destination, true) as MenuModel
            }
    }

    fun onDropDownMenuItemSelection(flag: Flag, variant: Variant) {
        if (flag.asString() == variant.toString()) {
            return
        }

        flag.setValue(variant)
        checkIfNeedRestart(flag)
    }

    companion object {
        private val RESTART_FLAGS = setOf(
            Flag.BOTTOM_NAVBAR_POSITION,
            Flag.DEV_MODE,
            Flag.Proxy,
            Flag.FORCE_DISABLE_SENTRY
        )
    }

    override fun onSliderValueChanged(flag: Flag, value: Int) {
        if (flag.asInt() == value) {
            return
        }

        flag.setValue(value)
        checkIfNeedRestart(flag = flag)
    }

    override fun navigateToSettingFragment(settingsDestination: SettingsDestination, p1: Bundle?) {
        when (settingsDestination) {
            SettingsDestination.OrangeThirdParty -> OrangeThirdPartySettingsFragment()
            SettingsDestination.OrangeChat -> OrangeChatSettingsFragment()
            SettingsDestination.OrangePlayer -> OrangePlayerSettingsFragment()
            SettingsDestination.OrangeView -> OrangeViewSettingsFragment()
            SettingsDestination.OrangeDev -> OrangeDevSettingsFragment()
            SettingsDestination.OrangeOTA -> OrangeUpdaterSettingsFragment()
            SettingsDestination.OrangeInfo -> OrangeInfoSettingsFragment()
            SettingsDestination.OrangeGestures -> OrangeGestureSettingsFragment()
            SettingsDestination.OrangeHighlighter -> highlighter.createHighlighterFragment()
            SettingsDestination.OrangeBlacklist -> blacklist.createBlacklistFragment()
            SettingsDestination.OrangeHighlightColor -> let {
                highlighter.showChangeMentionHighlightColorDialog(activity = activity)
                null
            }
            else -> null
        }?.let { fragment ->
            fragmentRouter.addOrRecreateFragmentWithDefaultTransitions(
                activity, fragment, settingsDestination.toString(), Bundle()
            )
        }
    }
}
