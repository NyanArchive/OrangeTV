package tv.orange.features.spam.bridge

import tv.twitch.android.shared.chat.command.ChatCommandAction

data class ChatSpamCommand(
    val messageText: String,
    val delay: Long = 1000,
    val count: Int = 1
) : ChatCommandAction(null)

data class ChatSpamErrorCommand(
    val text: String
) : ChatCommandAction(null)