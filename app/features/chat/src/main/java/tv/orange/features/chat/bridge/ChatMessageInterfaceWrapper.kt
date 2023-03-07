package tv.orange.features.chat.bridge

import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.shared.chat.pub.ChatMessageInterface

class ChatMessageInterfaceWrapper(
    private val cmi: ChatMessageInterface,
    private val badges: MutableList<MessageBadge>,
    private val tokens: MutableList<MessageToken>
) : ChatMessageInterface {
    override fun getBadges(): MutableList<MessageBadge> {
        return badges
    }

    override fun getDisplayName(): String {
        return cmi.displayName
    }

    override fun getTimestampSeconds(): Int {
        return cmi.timestampSeconds
    }

    override fun getTokens(): MutableList<MessageToken> {
        return tokens
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