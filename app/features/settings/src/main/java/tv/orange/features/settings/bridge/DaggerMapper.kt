package tv.orange.features.settings.bridge

import tv.orange.features.settings.bridge.settings.*
import tv.twitch.android.settings.base.BaseSettingsPresenter

enum class DaggerMapper(val fragment: Class<*>, val presenter: Class<out BaseSettingsPresenter>) {
    MainSettings(
        fragment = OrangeSettingsFragment::class.java,
        presenter = OrangeSettingsPresenter::class.java
    ),
    ThirdParty(
        fragment = OrangeThirdPartySettingsFragment::class.java,
        presenter = OrangeThirdPartySettingsPresenter::class.java
    ),
    Chat(
        fragment = OrangeChatSettingsFragment::class.java,
        presenter = OrangeChatSettingsPresenter::class.java
    ),
    Player(
        fragment = OrangePlayerSettingsFragment::class.java,
        presenter = OrangePlayerSettingsPresenter::class.java
    ),
    View(
        fragment = OrangeViewSettingsFragment::class.java,
        presenter = OrangeViewSettingsPresenter::class.java
    ),
    Dev(
        fragment = OrangeDevSettingsFragment::class.java,
        presenter = OrangeDevSettingsPresenter::class.java
    )
}