package tv.oranges.features.chathistory.data.mapper

import android.graphics.Color
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.orange.models.gql.twitch.fragment.ChatHistoryMessageFragment
import tv.oranges.features.chathistory.data.model.ChatHistoryMessage
import tv.oranges.features.chathistory.data.model.ChatMessageInterfaceWrapper
import tv.twitch.android.models.chat.AutoModMessageFlags
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.models.extensions.ExtensionModel
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt
import javax.inject.Inject

class ChatHistoryMapper @Inject constructor() {
    fun map(recentChatMessage: MessageBufferChatHistoryQuery.RecentChatMessage): ChatHistoryMessage {
        return ChatHistoryMessage(map(recentChatMessage.chatHistoryMessageFragment), Color.GRAY)
    }

    fun map(chatHistoryMessage: ChatHistoryMessage): ExtensionMessageKt {
        return ExtensionMessageKt(
            chatHistoryMessage.cmi,
            ExtensionModel("", "", "", null),
            Color.GRAY
        )
    }

    private fun map(fragment: List<ChatHistoryMessageFragment.Fragment>): List<MessageToken> {
        return fragment.map { tokenFragment ->
            tokenFragment.content?.let { content ->
                content.onEmote?.let { emote ->
                    MessageToken.EmoticonToken(emote.token, emote.emoteID)
                }
            } ?: MessageToken.TextToken(
                tokenFragment.text,
                AutoModMessageFlags(0, 0, 0, 0)
            )
        }
    }

    private fun mapBadges(badges: List<ChatHistoryMessageFragment.DisplayBadge?>): List<MessageBadge> {
        return badges.mapNotNull { badge ->
            MessageBadge(badge?.setID ?: "", badge?.version ?: "")
        }
    }

    private fun map(fragment: ChatHistoryMessageFragment?): ChatMessageInterface? {
        fragment ?: return null

        val tokens = map(fragment.content.fragments)
        val badges = mapBadges(fragment.sender?.displayBadges ?: emptyList())
        val displayName = fragment.sender?.displayName ?: "unknown"
        val userId = fragment.sender?.id?.toIntOrNull() ?: 0
        val userName = fragment.sender?.login ?: "unknown"

        return ChatMessageInterfaceWrapper(
            messageBadges = badges,
            messageTokens = tokens,
            timestamp = 0,
            messageDisplayName = displayName,
            messageUserName = userName,
            messageUserId = userId
        )
    }
}