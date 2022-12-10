package tv.orange.features.spam.bridge

import tv.twitch.android.shared.chat.command.ChatCommandAction

abstract class ChatSpamCommand : ChatCommandAction(null)

data class ChatSpamPyramidCommand(
    val emote: String,
    val delay: Long = 1000,
    val width: Int
) : ChatSpamCommand()

data class ChatSpamCommandImpl(
    val messageText: String,
    val delay: Long = 1000,
    val count: Int = 1
) : ChatSpamCommand()

data class ChatSpamErrorCommand(
    val text: String
) : ChatCommandAction(null)