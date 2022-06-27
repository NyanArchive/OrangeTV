package tv.oranges.features.chathistory.data.model

import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface

data class ChatMessageInterfaceWrapper(
    val messageBadges: List<MessageBadge>,
    val messageDisplayName: String,
    val timestamp: Int,
    val messageTokens: List<MessageToken>,
    val messageUserId: Int,
    val messageUserName: String
) : ChatMessageInterface {
    override fun getBadges(): MutableList<MessageBadge> {
        return messageBadges.toMutableList()
    }

    override fun getDisplayName(): String {
        return messageDisplayName
    }

    override fun getTimestampSeconds(): Int {
        return timestamp
    }

    override fun getTokens(): MutableList<MessageToken> {
        return messageTokens.toMutableList()
    }

    override fun getUserId(): Int {
        return messageUserId
    }

    override fun getUserName(): String {
        return messageUserName
    }

    override fun isAction(): Boolean {
        return false
    }

    override fun isDeleted(): Boolean {
        return false
    }

    override fun isSystemMessage(): Boolean {
        return false
    }
}
