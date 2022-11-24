package tv.orange.features.settings.bridge.settings

import tv.orange.features.settings.bridge.IFragment
import tv.twitch.android.settings.base.BaseSettingsFragment
import tv.twitch.android.settings.base.BaseSettingsPresenter

class OrangeGestureSettingsFragment : BaseSettingsFragment(), IFragment {
    lateinit var presenterImpl: BaseSettingsPresenter

    override fun createPresenter(): BaseSettingsPresenter {
        return presenterImpl
    }

    override fun setPresenter(presenter: BaseSettingsPresenter) {
        presenterImpl = presenter
    }
}