package tv.oranges.features.chathistory.data.mapper

import android.graphics.Color
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.orange.models.gql.twitch.fragment.ChatHistoryMessageFragment
import tv.oranges.features.chathistory.data.model.ChatHistoryMessage
import tv.twitch.android.models.chat.AutoModMessageFlags
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.models.extensions.ExtensionModel
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt
import javax.inject.Inject

class ChatHistoryMapper @Inject constructor() {
    fun map(recentChatMessage: MessageBufferChatHistoryQuery.RecentChatMessage): ChatHistoryMessage {
        return ChatHistoryMessage(parse(recentChatMessage.chatHistoryMessageFragment), Color.GRAY)
    }

    fun map(chatHistoryMessage: ChatHistoryMessage): ExtensionMessageKt {
        return ExtensionMessageKt(chatHistoryMessage.cmi, ExtensionModel("", "", "", null), Color.GRAY)
    }

    private fun parse(fragment: ChatHistoryMessageFragment?): ChatMessageInterface? {
        fragment ?: return null

        val tokens = mutableListOf<MessageToken>()
        fragment.content.fragments.forEach { tokenFragment ->
            tokenFragment.content?.let { content ->
                content.onEmote?.let { emote ->
                    tokens.add(
                        MessageToken.EmoticonToken(emote.token, emote.emoteID)
                    )
                }
            } ?: tokens.add(
                MessageToken.TextToken(
                    tokenFragment.text,
                    AutoModMessageFlags(0, 0, 0, 0)
                )
            )
        }
        val badges = mutableListOf<MessageBadge>()
        fragment.sender?.displayBadges?.forEach { displayBadge ->
            displayBadge?.setID.let { setID ->
                displayBadge?.version.let { version ->
                    badges.add(MessageBadge(setID, version))
                }
            }
        }

        val displayName = fragment.sender?.displayName ?: "unknown"
        val userId = fragment.sender?.id?.toIntOrNull() ?: 0
        val userName = fragment.sender?.login ?: "unknown"

        return fragment.sender?.let { sender ->
            object : ChatMessageInterface {
                override fun getBadges(): MutableList<MessageBadge> {
                    return badges
                }

                override fun getDisplayName(): String {
                    return displayName
                }

                override fun getTimestampSeconds(): Int {
                    return 0
                }

                override fun getTokens(): MutableList<MessageToken> {
                    return tokens
                }

                override fun getUserId(): Int {
                    return userId
                }

                override fun getUserName(): String {
                    return userName
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
        }
    }
}