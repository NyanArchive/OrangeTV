package tv.orange.features.chathistory.data.mapper

import android.graphics.Color
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.orange.models.gql.twitch.fragment.ChatHistoryMessageFragment
import tv.orange.models.util.DateUtil
import tv.twitch.chat.*
import javax.inject.Inject

class ChatHistoryMapper @Inject constructor() {
    fun map(response: MessageBufferChatHistoryQuery.RecentChatMessage): ChatMessageInfo {
        val fragment = response.chatHistoryMessageFragment

        val userNameColor = getUserNameColor(fragment.sender?.chatColor)
        val tokens = mapTokens(fragment.content.fragments).toTypedArray()
        val badges = mapBadges(fragment.sender?.displayBadges ?: emptyList()).toTypedArray()
        val displayName = fragment.sender?.displayName ?: "unknown"
        val userId = fragment.sender?.id?.toIntOrNull() ?: 0
        val userName = fragment.sender?.login ?: "unknown"
        val sentAt = getTimestamp(fragment.sentAt.toString())

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

    companion object {
        private fun getTimestamp(sentAt: String): Int {
            return DateUtil.getStandardizeDateString(str = sentAt)?.time?.div(1000)?.toInt() ?: 0
        }

        private fun getUserNameColor(chatColor: String?): Int {
            return chatColor?.let { color ->
                var res = Color.parseColor(color)
                if (res == Color.TRANSPARENT) {
                    res = Color.DKGRAY
                }
                res
            } ?: Color.DKGRAY
        }

        private fun mapTokens(fragments: List<ChatHistoryMessageFragment.Fragment>): List<ChatMessageToken> {
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

        private fun mapBadges(badges: List<ChatHistoryMessageFragment.DisplayBadge?>): List<ChatMessageBadge> {
            return badges.mapNotNull { badge ->
                ChatMessageBadge().apply {
                    this.name = badge?.setID ?: ""
                    this.version = badge?.version ?: ""
                }
            }
        }
    }
}