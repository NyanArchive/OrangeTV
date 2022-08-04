package tv.orange.features.chathistory.data.mapper

import android.graphics.Color
import tv.orange.core.util.DateUtil
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.orange.models.gql.twitch.fragment.ChatHistoryMessageFragment
import tv.twitch.chat.*
import javax.inject.Inject

class ChatHistoryMapper @Inject constructor() {
    fun map(recentChatMessage: MessageBufferChatHistoryQuery.RecentChatMessage): ChatMessageInfo? {
        return tokensMapper(recentChatMessage.chatHistoryMessageFragment)
    }

    private fun tokensMapper(fragments: List<ChatHistoryMessageFragment.Fragment>): List<ChatMessageToken> {
        return fragments.map { fragment ->
            fragment.content?.let { content ->
                content.onEmote?.let { emote ->
                    ChatEmoticonToken().apply {
                        this.emoticonId = emote.emoteID
                        this.emoticonText = emote.token
                    }
                }
            } ?: ChatTextToken().apply {
                this.text = fragment.text
            }
        }
    }

    private fun badgesMapper(badges: List<ChatHistoryMessageFragment.DisplayBadge?>): List<ChatMessageBadge> {
        return badges.mapNotNull { badge ->
            ChatMessageBadge().apply {
                this.name = badge?.setID ?: ""
                this.version = badge?.version ?: ""
            }
        }
    }

    private fun tokensMapper(fragment: ChatHistoryMessageFragment?): ChatMessageInfo? {
        fragment ?: return null

        var userNameColor = fragment.sender?.chatColor?.let { color ->
            var res = Color.parseColor(color)
            if (res == Color.TRANSPARENT) {
                res = Color.DKGRAY
            }
            res
        } ?: Color.DKGRAY
        val tokens = tokensMapper(fragment.content.fragments).toTypedArray()
        val badges = badgesMapper(fragment.sender?.displayBadges ?: emptyList()).toTypedArray()
        val displayName = fragment.sender?.displayName ?: "unknown"
        val userId = fragment.sender?.id?.toIntOrNull() ?: 0
        val userName = fragment.sender?.login ?: "unknown"
        val sentAt = DateUtil.getStandardizeDateString(
            fragment.sentAt.toString()
        )?.time?.div(1000)?.toInt() ?: 0

        return ChatMessageInfo().apply {
            this.displayName = displayName
            this.timestamp = sentAt
            this.userName = userName
            this.userId = userId
            this.badges = badges
            this.tokens = tokens
            this.nameColorARGB = userNameColor
        }
    }
}