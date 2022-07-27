package tv.orange.features.chat.bridge

import tv.twitch.android.shared.chat.settings.entry.ChatSettingsViewDelegate

open class ChatSettingsOrangeEvents : ChatSettingsViewDelegate.ChatSettingsEvents(null) {
    class Closable: ChatSettingsOrangeEvents()
    class Toggle: ChatSettingsOrangeEvents()
}