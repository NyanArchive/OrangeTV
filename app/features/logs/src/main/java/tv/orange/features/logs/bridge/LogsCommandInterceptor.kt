package tv.orange.features.logs.bridge

import androidx.fragment.app.FragmentActivity
import tv.orange.features.logs.ChatLogs
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandAction
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor

class LogsCommandInterceptor(
    private val activity: FragmentActivity,
    private val chatSource: LiveChatSource
) : ChatCommandInterceptor {
    override fun executeChatCommand(action: ChatCommandAction?) {
        when (action) {
            is ChatLocalLogsCommand -> {
                ChatLogs.get().showLocalLogs(
                    activity = activity,
                    channelId = action.channelId,
                    userName = action.userName
                )
            }
            is ChatTwitchLogsCommand -> {
                ChatLogs.get().showModLogs(
                    activity = activity,
                    channelId = action.channelId.toString(),
                    userName = action.userName
                )
            }
            is ChatUsageLogsCommand -> {
                chatSource.addSystemMessage("Usage: ${action.text}", false, null)
            }
        }
    }

    override fun onDestroy() {}

    override fun parseChatCommand(
        strArr: Array<out String>?,
        p1: ChannelInfo?,
        p2: Long?
    ): ChatCommandAction {
        strArr ?: return ChatCommandAction.NoOp.INSTANCE
        if (strArr.isEmpty()) {
            return ChatCommandAction.NoOp.INSTANCE
        }

        val command = strArr[0].lowercase().trim()
        if (command.isBlank()) {
            return ChatCommandAction.NoOp.INSTANCE
        }

        if (strArr.size < 2 || strArr[1].isBlank()) {
            return when (command) {
                "/llogs" -> ChatUsageLogsCommand(text = "/llogs @username")
                "/mlogs" -> ChatUsageLogsCommand(text = "/mlogs @username")
                else -> ChatCommandAction.NoOp.INSTANCE
            }
        }

        return when (command) {
            "/llogs" -> ChatLocalLogsCommand(
                channelId = p1?.id ?: -1,
                userName = getUserName(strArr[1])
            )
            "/mlogs" -> ChatTwitchLogsCommand(
                channelId = p1?.id ?: -1,
                userName = getUserName(strArr[1])
            )
            else -> ChatCommandAction.NoOp.INSTANCE
        }
    }

    companion object {
        fun getUserName(str: String): String {
            return str.lowercase().trim().let {
                if (it.startsWith("@")) {
                    return@let str.substring(1)
                } else {
                    return@let str
                }
            }
        }
    }
}