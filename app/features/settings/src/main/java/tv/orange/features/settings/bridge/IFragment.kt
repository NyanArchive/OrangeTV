package tv.orange.features.settings.bridge

import tv.twitch.android.settings.base.BaseSettingsPresenter

interface IFragment {
    fun setPresenter(presenter: BaseSettingsPresenter)
}