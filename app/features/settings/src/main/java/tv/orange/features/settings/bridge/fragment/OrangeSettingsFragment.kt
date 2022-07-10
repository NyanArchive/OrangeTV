package tv.orange.features.settings.bridge.fragment

import tv.twitch.android.settings.base.BaseSettingsFragment
import tv.twitch.android.settings.base.BaseSettingsPresenter
import javax.inject.Inject

class OrangeSettingsFragment : BaseSettingsFragment() {
    @Inject
    lateinit var presenter: OrangeSettingsPresenter

    override fun createPresenter(): BaseSettingsPresenter {
        return presenter
    }
}