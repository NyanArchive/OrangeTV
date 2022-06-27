package tv.oranges.features.chathistory.data.model

import tv.twitch.android.provider.chat.ChatMessageInterface

data class ChatHistoryMessage(
    val cmi: ChatMessageInterface?,
    val usernameColor: Int
)
