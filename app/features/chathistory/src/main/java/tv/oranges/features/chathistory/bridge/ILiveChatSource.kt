package tv.oranges.features.chathistory.bridge

import io.reactivex.disposables.Disposable
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt
import tv.twitch.chat.ChatMessageInfo

interface ILiveChatSource {
    fun addDisposable(disposable: Disposable)
    fun addChatHistoryMessage(message: ChatMessageInfo, channelId: Int)
}