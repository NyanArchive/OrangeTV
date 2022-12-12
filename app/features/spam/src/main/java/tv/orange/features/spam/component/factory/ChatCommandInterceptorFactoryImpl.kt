package tv.orange.features.spam.component.factory

import tv.orange.core.ResourceManager
import tv.orange.features.spam.bridge.SpamCommandInterceptor
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor
import javax.inject.Inject

class ChatCommandInterceptorFactoryImpl @Inject constructor(
    val resourceManager: ResourceManager
) : ChatCommandInterceptorFactory {
    override fun createSpamCommandInterceptor(chatSource: LiveChatSource): ChatCommandInterceptor {
        return SpamCommandInterceptor(chatSource = chatSource, rm = resourceManager)
    }
}