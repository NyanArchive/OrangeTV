package tv.orange.features.logs.bridge

import tv.twitch.android.shared.chat.command.ChatCommandAction

abstract class ChatLogsCommand : ChatCommandAction(null)

data class ChatLocalLogsCommand(
    val channelId: Int,
    val userName: String
) : ChatLogsCommand()

data class ChatTwitchLogsCommand(
    val channelId: Int,
    val userName: String
) : ChatLogsCommand()

data class ChatUsageLogsCommand(
    val text: String
) : ChatLogsCommand()