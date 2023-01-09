package tv.orange.features.spam.component.factory

import tv.orange.core.ResourcesManagerCore
import tv.orange.features.spam.bridge.SpamCommandInterceptor
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor
import javax.inject.Inject

class ChatCommandInterceptorFactoryImpl @Inject constructor(
    val resourcesManagerCore: ResourcesManagerCore
) : ChatCommandInterceptorFactory {
    override fun createSpamCommandInterceptor(chatSource: LiveChatSource): ChatCommandInterceptor {
        return SpamCommandInterceptor(chatSource = chatSource, rm = resourcesManagerCore)
    }
}