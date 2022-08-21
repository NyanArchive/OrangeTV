package tv.orange.features.spam.component.factory

import tv.orange.features.spam.bridge.SpamCommandInterceptor
import tv.orange.features.spam.di.scope.SpamScope
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor
import javax.inject.Inject

@SpamScope
class ChatCommandInterceptorFactoryImpl @Inject constructor() : ChatCommandInterceptorFactory {
    override fun createSpamCommandInterceptor(chatSource: LiveChatSource): ChatCommandInterceptor {
        return SpamCommandInterceptor(chatSource = chatSource)
    }
}