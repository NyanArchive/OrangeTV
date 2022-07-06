package tv.orange.features.chat.bridge

import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface

class ChatMessageInterfaceWrapper(
    private val cmi: ChatMessageInterface,
    private val badges: List<MessageBadge>,
    private val tokens: List<MessageToken>
) : ChatMessageInterface {
    override fun getBadges(): MutableList<MessageBadge> {
        return badges.toMutableList()
    }

    override fun getDisplayName(): String {
        return cmi.displayName
    }

    override fun getTimestampSeconds(): Int {
        return cmi.timestampSeconds
    }

    override fun getTokens(): MutableList<MessageToken> {
        return tokens.toMutableList()
    }

    override fun getUserId(): Int {
        return cmi.userId
    }

    override fun getUserName(): String {
        return cmi.userName
    }

    override fun isAction(): Boolean {
        return cmi.isAction
    }

    override fun isDeleted(): Boolean {
        return cmi.isDeleted
    }

    override fun isSystemMessage(): Boolean {
        return cmi.isSystemMessage
    }
}