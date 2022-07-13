package tv.orange.features.settings.bridge

import androidx.fragment.app.FragmentActivity
import tv.orange.core.Logger
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment
import tv.orange.features.settings.bridge.fragment.OrangeSettingsPresenter
import tv.orange.features.settings.component.SettingsController
import tv.twitch.android.settings.base.SettingsTracker
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder

class OrangeSettingsFragmentSubcomponentBindingImpl(
    private val fragmentActivity: FragmentActivity,
    private val adapterBinder: MenuAdapterBinder,
    private val settingsTracker: SettingsTracker,
    private val contoller: SettingsController
) : OrangeSettingsFragmentSubcomponentBinding {

    private fun providePresenter(): OrangeSettingsPresenter {
        return OrangeSettingsPresenter(fragmentActivity, adapterBinder, settingsTracker, contoller)
    }

    override fun inject(instance: OrangeSettingsFragment) {
        instance.presenter = providePresenter()
    }
}