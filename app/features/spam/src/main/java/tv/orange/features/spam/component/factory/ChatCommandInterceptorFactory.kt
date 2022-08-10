package tv.orange.features.spam.component.factory

import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor

interface ChatCommandInterceptorFactory {
    fun createSpamCommandInterceptor(chatSource: LiveChatSource): ChatCommandInterceptor
}