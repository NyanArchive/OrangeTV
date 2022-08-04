package tv.orange.features.chathistory.data.model

import tv.twitch.android.models.chat.AutoModMessageFlags
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface

data class ChatMessageInterfaceSystemMessage(val text: String) : ChatMessageInterface {
    override fun getBadges(): MutableList<MessageBadge> {
        return mutableListOf()
    }

    override fun getDisplayName(): String {
        return ""
    }

    override fun getTimestampSeconds(): Int {
        return 0
    }

    override fun getTokens(): MutableList<MessageToken> {
        return mutableListOf(MessageToken.TextToken(text, AutoModMessageFlags()))
    }

    override fun getUserId(): Int {
        return 0
    }

    override fun getUserName(): String {
        return ""
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
