package tv.orange.features.logs.component.data.mapper

import tv.orange.models.util.DateUtil
import tv.orange.features.logs.component.data.model.ChatMessage
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.model.UserInfo
import tv.orange.features.logs.di.scope.LogsScope
import tv.orange.models.gql.twitch.UserInfoQuery
import tv.orange.models.gql.twitch.ViewerCardModLogsMessagesBySenderQuery
import tv.orange.models.gql.twitch.fragment.ModChatHistoryMessageFragment
import tv.twitch.android.models.chat.AutoModMessageFlags
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.model.ChatHistoryMessage
import java.util.*
import javax.inject.Inject

@LogsScope
class LogsMapper @Inject constructor() {
    fun map(data: UserInfoQuery.Data): UserInfo {
        return UserInfo(userId = data.user!!.id, userName = data.user!!.login)
    }

    fun map(data: ViewerCardModLogsMessagesBySenderQuery.Data): List<MessageItem> {
        val messages = data.channel?.modLogs?.messagesBySender?.edges?.mapNotNull { edge ->
            edge.node?.let { node ->
                val fragment = node.modChatHistoryMessageFragment
                    ?: node.autoModCaughtChatHistoryMessageFragment?.modLogsMessage?.modChatHistoryMessageFragment
                fragment?.let { chatFragment ->
                    map(chatFragment, data.channel?.id?.toInt() ?: 0)
                }
            }
        } ?: emptyList()
        return convert(messages)
    }

    private fun convert(messages: List<ChatMessage>): List<MessageItem> {
        var currentDate: Date? = null
        val stack = mutableListOf<MessageItem>()
        messages.forEach{ message ->
            if (!DateUtil.isSameDate(currentDate, message.timestamp)) {
                stack.add(MessageItem.Header(message.timestamp))
                currentDate = message.timestamp
            }
            stack.add(MessageItem.Content(message))
        }

        return stack
    }

    private fun map(fragment: ModChatHistoryMessageFragment, channelId: Int): ChatMessage? {
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
                    AutoModMessageFlags()
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

        return fragment.sender?.let { sender ->
            ChatMessage(
                ChatHistoryMessage(
                    sender.id.toInt(),
                    sender.login,
                    sender.displayName,
                    sender.chatColor,
                    badges,
                    tokens
                ),
                DateUtil.getStandardizeDateString(fragment.sentAt.toString()) ?: Date(),
                channelId
            )
        }
    }
}