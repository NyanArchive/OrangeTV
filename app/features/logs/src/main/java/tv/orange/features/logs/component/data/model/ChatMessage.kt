package tv.orange.features.logs.component.data.model

import tv.twitch.android.provider.chat.model.ChatHistoryMessage
import java.util.*

data class ChatMessage(val token: ChatHistoryMessage, val timestamp: Date, val channelId: Int)