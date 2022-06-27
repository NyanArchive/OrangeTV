package tv.oranges.features.chathistory.data.mapper

import android.graphics.Color
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.oranges.features.chathistory.data.model.ChatHistoryMessage
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.models.extensions.ExtensionModel
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt
import javax.inject.Inject

class ChatHistoryMapper @Inject constructor() {
    fun map(recentChatMessage: MessageBufferChatHistoryQuery.RecentChatMessage): ChatHistoryMessage {
        return ChatHistoryMessage(
            displayName = recentChatMessage.sender?.displayName ?: "unknown",
            recentChatMessage.content.text,
            0
        )
    }

    fun map(message: ChatHistoryMessage): ExtensionMessageKt {
        return ExtensionMessageKt(object : ChatMessageInterface {
            override fun getBadges(): MutableList<MessageBadge> {
                return mutableListOf()
            }

            override fun getDisplayName(): String {
                TODO("Not yet implemented")
            }

            override fun getTimestampSeconds(): Int {
                TODO("Not yet implemented")
            }

            override fun getTokens(): MutableList<MessageToken> {
                TODO("Not yet implemented")
            }

            override fun getUserId(): Int {
                TODO("Not yet implemented")
            }

            override fun getUserName(): String {
                TODO("Not yet implemented")
            }

            override fun isAction(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isDeleted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isSystemMessage(): Boolean {
                TODO("Not yet implemented")
            }
        }, defaultExtensionModel, Color.TRANSPARENT)
    }

    companion object {
        private val defaultExtensionModel = ExtensionModel("", "", "", null)
    }
}