package tv.orange.features.settings.bridge.settings

import androidx.fragment.app.FragmentActivity
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.core.BooleanValue
import tv.orange.core.models.flag.core.IntRangeValue
import tv.orange.core.models.flag.core.ListValue
import tv.orange.core.models.flag.variants.FontSize
import tv.orange.features.settings.bridge.model.DropDownMenuModelExt
import tv.orange.features.settings.bridge.model.FlagToggleMenuModelExt
import tv.orange.features.settings.bridge.model.OrangeSubMenu
import tv.orange.features.settings.bridge.slider.SliderModel
import tv.orange.features.settings.component.OrangeSettingsController
import tv.twitch.android.settings.base.BaseSettingsPresenter
import tv.twitch.android.settings.base.SettingsNavigationController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.SettingsPreferencesController
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder
import tv.twitch.android.shared.ui.menus.core.MenuModel

open class BasedSettingsPresenter(
    activity: FragmentActivity,
    adapterBinder: MenuAdapterBinder,
    settingsTracker: SettingsTracker,
    private val orangeController: OrangeSettingsController,
    private val orangeSubMenuWrapper: OrangeSubMenu
) : BaseSettingsPresenter(activity, adapterBinder, settingsTracker) {
    override fun getNavController(): SettingsNavigationController {
        return orangeController
    }

    override fun getPrefController(): SettingsPreferencesController {
        return orangeController
    }

    override fun getToolbarTitle(): String {
        return ResourcesManagerCore.get().getString(resName = orangeSubMenuWrapper.title)
    }

    override fun updateSettingModels() {
        settingModels.clear()
        settingModels.addAll(convertToMenuModels(orangeSubMenuWrapper.items, orangeController))
    }

    companion object {
        fun convertToMenuModels(
            items: Collection<Flag>,
            controller: OrangeSettingsController
        ): Collection<MenuModel> {
            return items.mapNotNull { mapper(controller, it) }
        }

        private fun mapper(controller: OrangeSettingsController, flag: Flag): MenuModel? {
            return when (flag.valueHolder) {
                is BooleanValue -> FlagToggleMenuModelExt(flag)
                is ListValue<*> -> if (flag == Flag.CHAT_FONT_SIZE) {
                    DropDownMenuModelExt<FontSize>(flag, controller, true)
                } else {
                    DropDownMenuModelExt(flag, controller)
                }
                is IntRangeValue -> SliderModel(flag, controller)
                else -> null
            }
        }
    }
}