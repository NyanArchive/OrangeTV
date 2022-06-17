package tv.orange.features.emotes

import tv.orange.core.Logger
import tv.twitch.android.provider.chat.ChatMessageInterface

class Hook {
    fun injectEmotes(
        chatMessageInterface: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        Logger.debug(chatMessageInterface.toString())
        return chatMessageInterface
    }


    companion object {
        val instance by lazy {
            Hook()
        }
    }
}